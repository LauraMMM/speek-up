<?php
/**
 * Created by PhpStorm.
 * User: aiftemi
 * Date: 22/11/2015
 * Time: 12:25 AM
 */

namespace AppBundle\Controller;

use AppBundle\Util\CustomUtils;
use Sensio\Bundle\FrameworkExtraBundle\Configuration\Route;
use Symfony\Bundle\FrameworkBundle\Controller\Controller;
use Symfony\Component\HttpFoundation\Request;
use Symfony\Component\HttpFoundation\JsonResponse;

use Acme\SpeekUpMongoBundle\Document\Activity;
use AppBundle\Util\CustomValidator;

class ActivityserviceController extends Controller
{
    /**
     * @Route("/activityservice", name="activityserviceindex")
     */
    public function indexAction(Request $request)
    {
        return new JsonResponse(array("serviceName" => "activityservice"));
    }

    /**
     * @Route("/activityservice/add", name="activityserviceadd")
     */
    public function addAction(Request $request)
    {
        //get post data
        $userId = $request->request->get('userId');
        $eventId = $request->request->get('eventId');
        $text = $request->request->get('text');
        $type = $request->request->get('type');

        //build validation array
        $validationArray = array(
            "userId" => $userId,
            "eventId" => $eventId,
            "text" => $text,
            "type" => $type,
        );

        //simulate some validations :D
        $methodResponse = CustomValidator::validateEmptyVarsForResponse($validationArray);
        if (!empty($methodResponse)) return new JsonResponse($methodResponse);

        //retrieve MongoDB object
        $doctrineMongo = $this->get('doctrine_mongodb');

        //find user and exit controller if failed
        $methodResponse = CustomValidator::validateObjectForService($doctrineMongo,"User",$userId);
        if ($methodResponse["status"] == "0") return new JsonResponse($methodResponse);

        //find event and exit controller if failed
        $methodResponse = CustomValidator::validateObjectForService($doctrineMongo,"Event",$eventId);
        if ($methodResponse["status"] == "0") return new JsonResponse($methodResponse);

        //find event and exit controller if failed
        $methodResponse = CustomValidator::validateObjectForService($doctrineMongo,"Event",$eventId);
        if ($methodResponse["status"] == "0") return new JsonResponse($methodResponse);

        //get possible existing activities (table/collection)
        $existingActivities = CustomUtils::getCollectionObjectsAsArray($doctrineMongo,"Activity",array("text" => $text));

        //init response
        $response = array();

        //test user existence
        if (count($existingActivities) > 0)
        {
            //if user does not exists return standard error response
            $response["status"] = "0";
            $response["messages"] = array("Similar suggestion already exists");
        }
        else
        {
            $newSuggestion = new Activity();
            $newSuggestion->setEventId($eventId);
            $newSuggestion->setUserId($userId);
            $newSuggestion->setText($text);
            $newSuggestion->setType($type);
            $newSuggestion->setStatus("voting");
            $newSuggestion->setExpireAt(time() + (60 * 5)); //5 min
            $newSuggestion->setYesCount(0);
            $newSuggestion->setNoCount(0);

            //save suggestion
            $doctrineMongo->getManager()->persist($newSuggestion);

            //commit changes
            $doctrineMongo->getManager()->flush();

            $response["status"] = "1";
            $response["messages"] = array("Suggestion proposed");
            $response["data"] = array("activityId" => $newSuggestion->getId());
        }

        //render json response
        return new JsonResponse($response);
    }

    /**
     * @Route("/activityservice/list", name="activityservicelist")
     */
    public function listAction(Request $request)
    {
        //get post data
        $userId = $request->request->get('userId');
        $eventId = $request->request->get('eventId');

        //build validation array
        $validationArray = array(
            "userId" => $userId,
            "eventId" => $eventId,
        );

        //simulate some validations :D
        $methodResponse = CustomValidator::validateEmptyVarsForResponse($validationArray);
        if (!empty($methodResponse)) return new JsonResponse($methodResponse);

        //retrieve MongoDB object
        $doctrineMongo = $this->get('doctrine_mongodb');

        //find user and exit controller if failed
        $methodResponse = CustomValidator::validateObjectForService($doctrineMongo,"User",$userId);
        if ($methodResponse["status"] == "0") return new JsonResponse($methodResponse);

        //find event and exit controller if failed
        $methodResponse = CustomValidator::validateObjectForService($doctrineMongo,"Event",$eventId);
        if ($methodResponse["status"] == "0") return new JsonResponse($methodResponse);

        //get activities belonging to provided event and not yet expired
        $activitiesResponse = CustomUtils::getCollectionObjectsAsArray($doctrineMongo,"Activity",array("eventId" => $eventId));

        //perform an ugly extra step to remove expired activities instead of defining a much more ugly cron to do the job
        //use an ugly index to keep cursor of this array to update voted tag for Dan
        $arrayIndex = 0;
        foreach($activitiesResponse as $activityArray)
        {
            //test if activity is expired
            if($activityArray["expired"])
            {
                //delete the activity
                CustomUtils::deleteCollectionObjectById($doctrineMongo,"Activity",$activityArray["id"]);
            }
            else
            {
                //test if current user voted on the suggestion
                if (in_array($userId,$activityArray["voters"]))
                {
                    $activitiesResponse[$arrayIndex]["voted"] = "yes";
                }
                else
                {
                    $activitiesResponse[$arrayIndex]["voted"] = "no";
                }
            }
            $arrayIndex++;
        }

        //render json response - all activities
        $response["status"] = "1";
        $response["messages"] = array("Activities listed");
        $response["data"] = $activitiesResponse;
        return new JsonResponse($response);
    }

    /**
     * @Route("/activityservice/vote", name="activityservicevote")
     */
    public function voteAction(Request $request)
    {
        //get post data
        $userId = $request->request->get('userId');
        $suggestionId = $request->request->get('suggestionId');
        $vote = $request->request->get('vote');

        //build validation array
        $validationArray = array(
            "userId" => $userId,
            "suggestionId" => $suggestionId,
            "vote" => $vote,
        );

        //simulate some validations :D
        $methodResponse = CustomValidator::validateEmptyVarsForResponse($validationArray);
        if (!empty($methodResponse)) return new JsonResponse($methodResponse);

        //retrieve MongoDB object
        $doctrineMongo = $this->get('doctrine_mongodb');

        //find user and exit controller if failed
        $methodResponse = CustomValidator::validateObjectForService($doctrineMongo,"User",$userId);
        if ($methodResponse["status"] == "0") return new JsonResponse($methodResponse);

        //find activity and exit controller if failed
        $methodResponse = CustomValidator::validateObjectForServiceById($doctrineMongo,"Activity",$suggestionId);
        if ($methodResponse["status"] == "0") return new JsonResponse($methodResponse);

        //get activity record
        $currentActivity = $methodResponse["data"];

        //find event and exit controller if failed
        $methodResponse = CustomValidator::validateObjectForService($doctrineMongo,"Event",$currentActivity->getEventId());
        if ($methodResponse["status"] == "0") return new JsonResponse($methodResponse);

        //get activity event record
        $currentEvent = $methodResponse["data"];

        //test if suggestion is still available by expired
        if ($currentActivity->isExpired())
        {
            $response["status"] = "0";
            $response["messages"] = array("Suggestion expired");
            return new JsonResponse($response);
        }

        //test if suggestion is a winner, then there is no need for votes
        if ($currentActivity->getStatus() == "winner")
        {
            $response["status"] = "0";
            $response["messages"] = array("Suggestion is a winner already");
            return new JsonResponse($response);
        }

        //get activity voters
        $activityVoters = $currentActivity->getVoters();

        //use a var to determine if we require to flush the db and to form user response
        $wePerformUpdates = false;

        //check if current user is already attending to the event
        if (!in_array($userId,$activityVoters))
        {
            //if not add add event to user and save
            $activityVoters[] = $userId;
            $currentActivity->setVoters($activityVoters);

            //register user vote
            if ($vote == "yes")
            {
                $currentActivity->setYesCount($currentActivity->getYesCount() + 1);
            }
            else
            {
                $currentActivity->setNoCount($currentActivity->getNoCount() + 1);
            }

            //if number of voters is bigger than half of the people attending at the event
            //and of course we have more then 3 voters TODO change to more when more users will use the app
            if (
                    (count($activityVoters) >= 3) &&
                    (count($activityVoters) > intval($currentEvent->getLocalCount()/2))
                )
            {
                //test to see if we have a winner
                if ($currentActivity->getYesCount() > $currentActivity->getNoCount())
                {
                    //set the suggestion to winner and set the expiration time to 60 seconds
                    $currentActivity->setStatus("winner");
                    $currentActivity->setExpireAt(time() + 60);
                }
            }
            //save suggestion vote and commit
            $doctrineMongo->getManager()->persist($currentActivity);
            $doctrineMongo->getManager()->flush();

            //update flag
            $wePerformUpdates = true;
        }

        //init final response
        $response = array();
        if ($wePerformUpdates === true)
        {
            $response["status"] = "1";
            $response["messages"] = array("Vote saved");
        }
        else
        {
            $response["status"] = "0";
            $response["messages"] = array("User already voted to suggestion");
        }

        //render json response
        return new JsonResponse($response);
    }
}