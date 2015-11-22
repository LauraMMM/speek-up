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
     * @Route("/activityservice/add", name="activityservice/add")
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

        //get possible existing activties (table/collection)
        $existingActivties = CustomUtils::getCollectionObjectsAsArray($doctrineMongo,"Activity",array("text" => $text, "expired" == false));

        //init response
        $response = array();

        //test user existence
        if (count($existingActivties) > 0)
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
            $newSuggestion->setExpireAt(time() + (1000 * 60 * 5)); //5 min
            $newSuggestion->setYesCount(0);
            $newSuggestion->setNoCount(0);
            $newSuggestion->setExpired(false);

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
        $activitiesResponse = CustomUtils::getCollectionObjectsAsArray($doctrineMongo,"Activity",array("eventId" => $eventId,"expired" => false));

        //render json response - all events
        return new JsonResponse(array("activities" => $activitiesResponse));
    }
}