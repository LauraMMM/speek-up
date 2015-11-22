<?php
/**
 * Created by PhpStorm.
 * User: aiftemi
 * Date: 22/11/2015
 * Time: 6:43 AM
 */

namespace AppBundle\Tests\Controller;

use Symfony\Bundle\FrameworkBundle\Test\WebTestCase;

class EventserviceControllerTest extends WebTestCase
{
    public function testCreateEvent()
    {
        //create http client
        $client = static::createClient();

        //call event service register method
        $crawler = $client->request('POST','/eventservice/register', array(
            "userId" => "fbId12345",
            "fbId" => "fbEventId.1",
            "fbTitle" => "Protest PUIE MONTA",
            "fbDescription" => "Descriere protest`",
            "fbAvatar" => "link avatar protest",
            "fbCover" => "link poza protest",
            "fbStartTime" => time(),
            "fbEndTime" => time() + (60 * 60 * 2), //2h
            "fbLocationName" => "Universitate",
            "fbLatitude" => "lat",
            "fbLongitude" => "long",
            "fbCount" => 3,
        ));

        //get the actual response
        $actual = $client->getResponse()->getContent();

        //define expected result
        $expected = '{"status":"1","messages":["Event registered"],"data":{"eventId":"fbEventId.1"}}';

        //check values
        $this->assertEquals($expected,$actual);
    }
}