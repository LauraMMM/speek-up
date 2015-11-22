<?php
/**
 * Created by PhpStorm.
 * User: aiftemi
 * Date: 22/11/2015
 * Time: 6:55 AM
 */

namespace AppBundle\Tests\Controller;

use Symfony\Bundle\FrameworkBundle\Test\WebTestCase;

class ActivityserviceControllerTest extends WebTestCase
{
    public function testCreateActivity()
    {
        //create http client
        $client = static::createClient();

        //call activity service register method
        $crawler = $client->request('POST','/eventservice/register', array(
            "userId" => "fbId12345",
            "eventId" => "fbEventId.1",
            "text" => "Sa mergem la Universitate",
            "type" => "move",
        ));

        //get the actual response
        $actual = $client->getResponse()->getContent();

        //define expected result
        $expected = '{"status":"1","messages":["Suggestion proposed"],"data":{"activityId":"i_should_make_inserts_stuv_id"}}';

        //check values
        $this->assertEquals($expected,$actual);
    }
}