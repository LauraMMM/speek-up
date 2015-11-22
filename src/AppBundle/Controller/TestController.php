<?php
namespace AppBundle\Controller;

use Sensio\Bundle\FrameworkExtraBundle\Configuration\Route;
use Symfony\Bundle\FrameworkBundle\Controller\Controller;
use Symfony\Component\HttpFoundation\Request;
use Symfony\Component\HttpFoundation\JsonResponse;
use Symfony\Component\HttpKernel\Client;

class TestController extends Controller
{
    /**
     * @Route("/test/login", name="testloginpage")
     */
    public function loginAction(Request $request)
    {
        $client = new Client($this->get('kernel'));
        $client->request('POST','/userservice/login',array(
            "fbId" => "fbId12345.3",
            "fbName" => "Alin Iftemi",
            "fbAvatar" => "fbAvatarLink12345",
            "deviceId" => "userDeviceId12345",
        ));

        //print $client->getResponse()->getContent();
        return new JsonResponse(json_decode($client->getResponse()->getContent()));
    }

    /**
     * @Route("/test/eventserviceregister", name="testeventserviceregister")
     */
    public function eventserviceregisterAction(Request $request)
    {
        $userId = "fbId12345";
        $eventId = "fbEventId1";
        $client = new Client($this->get('kernel'));
        $client->request('POST','/eventservice/register',array(
            "userId" => $userId,
            "fbId" => $eventId,
            "fbTitle" => "Protest anti ponta",
            "fbDescription" => "Descriere protest`",
            "fbAvatar" => "link avatr protest",
            "fbCover" => "link poza portest",
            "fbStartTime" => time(),
            "fbEndTime" => time() + (1000 * 60 * 60 * 5), //5h
            "fbLocationName" => "Universitate",
            "fbLatitude" => "lat",
            "fbLongitude" => "long",
            "fbCount" => 3,
        ));
        //print $client->getResponse()->getContent();
        return new JsonResponse(json_decode($client->getResponse()->getContent()));
    }

    /**
     * @Route("/test/eventserviceattend", name="testeventserviceattend")
     */
    public function eventserviceattendAction(Request $request)
    {
        $userId = "fbId12345";
        $eventId = "fbEventId1";
        $client = new Client($this->get('kernel'));
        $client->request('POST','/eventservice/attend',array(
            "userId" => $userId,
            "eventId" => $eventId,
        ));
        //print $client->getResponse()->getContent();
        return new JsonResponse(json_decode($client->getResponse()->getContent()));
    }

    /**
     * @Route("/test/eventserviceunattend", name="testeventserviceunattend")
     */
    public function eventserviceunattendAction(Request $request)
    {
        $userId = "fbId12345";
        $eventId = "fbEventId1";
        $client = new Client($this->get('kernel'));
        $client->request('POST','/eventservice/unattend',array(
            "userId" => $userId,
            "eventId" => $eventId,
        ));
        //print $client->getResponse()->getContent();
        return new JsonResponse(json_decode($client->getResponse()->getContent()));
    }

    /**
     * @Route("/test/eventservicelist", name="testeventservicelist")
     */
    public function eventservicelistAction(Request $request)
    {
        $userId = "fbId12345";
        $client = new Client($this->get('kernel'));
        $client->request('POST','/eventservice/list',array(
            "userId" => $userId,
        ));
        print "<pre>";
        print_r(json_decode($client->getResponse()->getContent(),true));
        print "</pre>";
        //print $client->getResponse()->getContent();
        return new JsonResponse(json_decode($client->getResponse()->getContent()));
    }

    /**
     * @Route("/test/activityservicelist", name="testactivityservicelist")
     */
    public function activityservicelistAction(Request $request)
    {
        $userId = "fbId12345";
        $eventId = "fbEventId1";

        $client = new Client($this->get('kernel'));
        $client->request('POST','/activityservice/list',array(
            "userId" => $userId,
            "eventId" => $eventId,
        ));

        print "<pre>";
        print_r(json_decode($client->getResponse()->getContent(),true));
        print "</pre>";
        //print $client->getResponse()->getContent();
        return new JsonResponse(json_decode($client->getResponse()->getContent()));
    }

    /**
     * @Route("/test/activityserviceadd", name="testactivityserviceadd")
     */
    public function activityserviceaddAction(Request $request)
    {
        $userId = "fbId12345";
        $eventId = "fbEventId1";
        $textRef = "JOS PONTA 3";

        $client = new Client($this->get('kernel'));
        $client->request('POST','/activityservice/add',array(
            "userId" => $userId,
            "eventId" => $eventId,
            "text" => $textRef,
            "type" => "speak",
        ));

        //print $client->getResponse()->getContent();
        return new JsonResponse(json_decode($client->getResponse()->getContent()));
    }

    /**
     * @Route("/test/activityservicevote", name="testactivityservicevote")
     */
    public function activityservicevoteAction(Request $request)
    {
        $userId = "fbId12345.3";
        $suggestionId = "565135967a6353241c000042";
        $vote = "yes";

        $client = new Client($this->get('kernel'));
        $client->request('POST','/activityservice/vote',array(
            "userId" => $userId,
            "suggestionId" => $suggestionId,
            "vote" => $vote,
        ));

        //print $client->getResponse()->getContent();
        return new JsonResponse(json_decode($client->getResponse()->getContent()));
    }
}