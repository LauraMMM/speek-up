<?php
/**
 * Created by PhpStorm.
 * User: aiftemi
 * Date: 22/11/2015
 * Time: 12:25 AM
 */

namespace AppBundle\Controller;

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

        //assign current user to a reasonable variable name
        $currentUser = $methodResponse["data"];

        //find event and exit controller if failed
        $methodResponse = CustomValidator::validateObjectForService($doctrineMongo,"Event",$eventId);
        if ($methodResponse["status"] == "0") return new JsonResponse($methodResponse);

        //assign current event to a reasonable variable name
        $currentEvent = $methodResponse["data"];

        //get event repository (table/collection)
        $activityRepository = $doctrineMongo->getRepository('AcmeSpeekUpMongoBundle:Activity');

        //init response activties list
        $activtiesResponse = array();

        // find all events
        $activties = $activityRepository->findAll();
        foreach ($activties as $activityObjectFound)
        {
            $activtiesResponse[] = $activityObjectFound->toArray();
        }

        //render json response - all events
        return new JsonResponse(array("activties" => $activtiesResponse));
    }
}