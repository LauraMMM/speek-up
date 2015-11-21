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
        $userRef = "3";
        $client = new Client($this->get('kernel'));
        $client->request('POST','/userservice/login',array(
            "fbId" => "fb".$userRef,
            "fbName" => "user".$userRef,
            "fbAvatar" => "avatarLink".$userRef,
            "deviceId" => "device".$userRef,
        ));

        //print $client->getResponse()->getContent();
        return new JsonResponse(json_decode($client->getResponse()->getContent()));
    }

    /**
     * @Route("/test/eventserviceregister", name="testeventserviceregister")
     */
    public function eventserviceregisterAction(Request $request)
    {
        $userRef = "2";
        $eventRef = "5";
        $client = new Client($this->get('kernel'));
        $client->request('POST','/eventservice/register',array(
            "userId" => "fb".$userRef,
            "fbId" => "eventFb".$eventRef,
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
        $userRef = "3";
        $eventRef = "4";
        $client = new Client($this->get('kernel'));
        $client->request('POST','/eventservice/attend',array(
            "userId" => "fb".$userRef,
            "eventId" => "eventFb".$eventRef,
        ));
        //print $client->getResponse()->getContent();
        return new JsonResponse(json_decode($client->getResponse()->getContent()));
    }

    /**
     * @Route("/test/eventserviceunattend", name="testeventserviceunattend")
     */
    public function eventserviceunattendAction(Request $request)
    {
        $userRef = "2";
        $eventRef = "1";
        $client = new Client($this->get('kernel'));
        $client->request('POST','/eventservice/unattend',array(
            "userId" => "fb".$userRef,
            "eventId" => "eventFb".$eventRef,
        ));
        //print $client->getResponse()->getContent();
        return new JsonResponse(json_decode($client->getResponse()->getContent()));
    }

    /**
     * @Route("/test/eventservicelist", name="testeventservicelist")
     */
    public function eventservicelistAction(Request $request)
    {
        $userRef = "1";
        $client = new Client($this->get('kernel'));
        $client->request('POST','/eventservice/list',array(
            "userId" => "fb".$userRef,
        ));
        //print $client->getResponse()->getContent();
        return new JsonResponse(json_decode($client->getResponse()->getContent()));
    }


    /**
     * @Route("/test/activityservicelist", name="testactivityservicelist")
     */
    public function activityservicelistAction(Request $request)
    {
        $client = new Client($this->get('kernel'));
        $client->request('POST','/activityservice/list',array(
            "userId" => "fb1",
            "eventId" => "eventFbid2",
        ));
        //print $client->getResponse()->getContent();
        return new JsonResponse(json_decode($client->getResponse()->getContent()));
    }
}