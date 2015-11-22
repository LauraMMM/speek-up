<?php
namespace AppBundle\Tests\Controller;

use Symfony\Bundle\FrameworkBundle\Test\WebTestCase;
/**
 * Created by PhpStorm.
 * User: aiftemi
 * Date: 21/11/2015
 * Time: 5:49 PM
 */
class UserserviceControllerTest extends WebTestCase
{
    public function testCreateLogin()
    {
        //create http client
        $client = static::createClient();

        //call user service login method
        $crawler = $client->request('POST','/userservice/login', array(
            "fbId" => "fbId12345",
            "fbName" => "Alin Iftemi",
            "fbAvatar" => "fbAvatarLink12345",
            "deviceId" => "userDeviceId12345",
        ));

        //get the actual response
        $actual = $client->getResponse()->getContent();

        //define expected result
        $expected = '{"status":"1","messages":["User created"],"data":{"userId":"fbId12345"}}';

        //check values
        $this->assertEquals($expected,$actual);
    }

    public function testUpdateLogin()
    {
        //create http client
        $client = static::createClient();

        //call user service login method
        $crawler = $client->request('POST','/userservice/login', array(
            "fbId" => "fbId12345",
            "fbName" => "Alin Iftemi",
            "fbAvatar" => "fbAvatarLink12345",
            "deviceId" => "userDeviceId12345",
        ));

        //get the actual response
        $actual = $client->getResponse()->getContent();

        //define expected result - this time we update user data
        $expected = '{"status":"1","messages":["User updated"],"data":{"userId":"fbId12345"}}';

        //check values
        $this->assertEquals($expected,$actual);
    }
}