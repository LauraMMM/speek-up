<?php
namespace AppBundle\Controller;

use Sensio\Bundle\FrameworkExtraBundle\Configuration\Route;
use Symfony\Bundle\FrameworkBundle\Controller\Controller;
use Symfony\Component\HttpFoundation\Request;
use Symfony\Component\HttpFoundation\JsonResponse;

use Acme\SpeekUpMongoBundle\Document\Event;
use AppBundle\Util\CustomValidator;
use AppBundle\Util\CustomUtils;

class EventserviceController extends Controller
{
    /**
     * @Route("/eventservice", name="eventserviceindex")
     */
    public function indexAction(Request $request)
    {
        return new JsonResponse(array("serviceName"=>"eventservice"));
    }

    /**
     * @Route("/eventservice/unattend", name="eventservice/unattend")
     */
    public function unattendAction(Request $request)
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

        //assign current user to a reasonable variable name
        $currentUser = $methodResponse["data"];

        //find event and exit controller if failed
        $methodResponse = CustomValidator::validateObjectForService($doctrineMongo,"Event",$eventId);
        if ($methodResponse["status"] == "0") return new JsonResponse($methodResponse);

        //assign current event to a reasonable variable name
        $currentEvent = $methodResponse["data"];

        //use a var to determine if we require to flush the db and to form user response
        $wePerformUpdates = false;

        //get user events
        $userEvents = $currentUser->getEvents();
        //check if current user is attending to the event
        if (($key = array_search($eventId, $userEvents)) !== false)
        {
            unset($userEvents[$key]);
            $currentUser->setEvents($userEvents);
            $doctrineMongo->getManager()->persist($currentUser);

            //update flag
            $wePerformUpdates = true;
        }

        //add event attendee
        $eventAttendees = $currentEvent->getAttendees();

        //check the event contains current user in it's list
        if (($key = array_search($userId, $eventAttendees)) !== false)
        {
            unset($eventAttendees[$key]);
            $currentEvent->setAttendees($eventAttendees);
            $doctrineMongo->getManager()->persist($currentEvent);

            //update flag
            $wePerformUpdates = true;
        }

        //init final response
        $response = array();
        if ($wePerformUpdates === true)
        {
            //commit changes
            $doctrineMongo->getManager()->flush();

            $response["status"] = "1";
            $response["messages"] = array("User unsubscribed from event");
        }
        else
        {

            $response["status"] = "0";
            $response["messages"] = array("User is not subscribed to the event");
        }

        //render json response
        return new JsonResponse($response);
    }

    /**
     * @Route("/eventservice/attend", name="eventserviceattend")
     */
    public function attendAction(Request $request)
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

        //assign current user to a reasonable variable name
        $currentUser = $methodResponse["data"];

        //find event and exit controller if failed
        $methodResponse = CustomValidator::validateObjectForService($doctrineMongo,"Event",$eventId);
        if ($methodResponse["status"] == "0") return new JsonResponse($methodResponse);

        //assign current event to a reasonable variable name
        $currentEvent = $methodResponse["data"];

        //use a var to determine if we require to flush the db and to form user response
        $wePerformUpdates = false;

        //get user events
        $userEvents = $currentUser->getEvents();
        //check if current user is already attending to the event
        if (!in_array($eventId,$userEvents))
        {
            //if not add add event to user and save
            $userEvents[] = $eventId;
            $currentUser->setEvents($userEvents);
            $doctrineMongo->getManager()->persist($currentUser);

            //update flag
            $wePerformUpdates = true;
        }

        //add event attendee
        $eventAttendees = $currentEvent->getAttendees();
        //check the event contains current user in it's list
        if (!in_array($userId,$eventAttendees))
        {
            //if not add user in events attendees list and save
            $eventAttendees[] = $userId;
            $currentEvent->setAttendees($eventAttendees);
            $doctrineMongo->getManager()->persist($currentEvent);

            //update flag
            $wePerformUpdates = true;
        }

        //init final response
        $response = array();
        if ($wePerformUpdates === true)
        {
            //commit changes
            $doctrineMongo->getManager()->flush();

            $response["status"] = "1";
            $response["messages"] = array("User subscribed to event");
        }
        else
        {

            $response["status"] = "0";
            $response["messages"] = array("User already subscribed to the event");
        }

        //render json response
        return new JsonResponse($response);

    }

    /**
     * @Route("/eventservice/register", name="eventserviceregister")
     */
    public function registerAction(Request $request)
    {
        //get post data
        $userId = $request->request->get('userId');
        $fbId = $request->request->get('fbId');
        $fbTitle = $request->request->get('fbTitle');
        $fbDescription = $request->request->get('fbDescription');
        $fbAvatar = $request->request->get('fbAvatar');
        $fbCover = $request->request->get('fbCover');
        $fbStartTime = $request->request->get('fbStartTime');
        $fbEndTime = $request->request->get('fbEndTime');
        $fbLocationName = $request->request->get('fbLocationName');
        $fbLatitude = $request->request->get('fbLatitude');
        $fbLongitude = $request->request->get('fbLongitude');
        $fbCount = $request->request->get('fbCount');

        //build validation array
        $validationArray = array(
            "userId" => $userId,
            "fbId" => $fbId,
            "fbTitle" => $fbTitle,
            "fbDescription" => $fbDescription,
            "fbAvatar" => $fbAvatar,
            "fbCover" => $fbCover,
            "fbStartTime" => $fbStartTime,
            "fbEndTime" => $fbEndTime,
            "fbLocationName" => $fbLocationName,
            "fbLatitude" => $fbLatitude,
            "fbLongitude" => $fbLongitude,
            "fbCount" => $fbCount,
        );

        //simulate some validations :D
        $methodResponse = CustomValidator::validateEmptyVarsForResponse($validationArray);
        if (!empty($methodResponse)) return new JsonResponse($methodResponse);

        //init page response
        $response = array();

        //extra validation for event count - do not allow less than 100 users attended
        if (intval($fbCount) < 3)   //TODO when users will show up remove this and configure this things somewhere, maybe a table ????? :)
        {
            $response["status"] = "0";
            $response["messages"] = array("You need at least 100 attendees to your protest in order add it to SpeekUp");
            return new JsonResponse($response);
        }

        //retrieve MongoDB object
        $doctrineMongo = $this->get('doctrine_mongodb');

        //find user and exit controller if failed
        $methodResponse = CustomValidator::validateObjectForService($doctrineMongo,"User",$userId);
        if ($methodResponse["status"] == "0") return new JsonResponse($methodResponse);

        //find event and exit controller if failed
        $methodResponse = CustomValidator::validateObjectForService($doctrineMongo,"Event",$fbId);

        //test if event already saved
        if ($methodResponse["status"] == "1")
        {
            //if event found update latest facebook details
            $response["status"] = "0";
            $response["messages"] = array("Event already added to SpeekUp");
        }
        else
        {
            //if user not found create it - add fb id only once
            $event = new Event();
            $event->setUserId($userId);
            $event->setFbId($fbId);
            $event->setFbTitle($fbTitle);
            $event->setFbDescription($fbDescription);
            $event->setFbAvatar($fbAvatar);
            $event->setFbCover($fbCover);
            $event->setFbStartTime($fbStartTime);
            $event->setFbEndTime($fbEndTime);
            $event->setFbLocationName($fbLocationName);
            $event->setFbLatitude($fbLatitude);
            $event->setFbLongitude($fbLongitude);
            $event->setFbCount($fbCount);
            $event->setLocalCount(0); //set local count to 0

            //save event
            $doctrineMongo->getManager()->persist($event);

            //commit changes
            $doctrineMongo->getManager()->flush();

            $response["status"] = "1";
            $response["messages"] = array("Event registered");
            $response["data"] = array("eventId" => $event->getFbId());
        }

        //render json response
        return new JsonResponse($response);
    }

    /**
     * @Route("/eventservice/list", name="eventservicelist")
     */
    public function listAction(Request $request)
    {
        //get post data
        $userId = $request->request->get('userId');

        //build validation array
        $validationArray = array(
            "userId" => $userId,
        );

        //simulate some validations :D
        $methodResponse = CustomValidator::validateEmptyVarsForResponse($validationArray);
        if (!empty($methodResponse)) return new JsonResponse($methodResponse);

        //retrieve MongoDB object
        $doctrineMongo = $this->get('doctrine_mongodb');

        //find user and exit controller if failed
        $methodResponse = CustomValidator::validateObjectForService($doctrineMongo,"User",$userId);
        if ($methodResponse["status"] == "0") return new JsonResponse($methodResponse);

        //get all events in teh system
        $eventsResponse = CustomUtils::getAllCollectionObjectsAsArray($doctrineMongo,"Event");

        //render json response - all events
        return new JsonResponse(array("events" => $eventsResponse));
    }
}