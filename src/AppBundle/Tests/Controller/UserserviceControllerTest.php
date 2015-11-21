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
    public function testLogin()
    {
        //create http client
        $client = static::createClient();
        //$crawler = $client->request('GET', '/post/hello-world');

        //call user service login method
        $crawler = $client->request('POST','/userservice/login', array(
            "fbId" => "fbId",
            "fbName" => "Alin Iftemi",
            "fbAvatar" => "link to avatar",
            "deviceId" => "1234567890",
        ));

        //get the actual response
        $actual = $client->getResponse()->getContent();

        //define expected result
        $expected = '{"status":"0","message":"Invalid param fbId"}';

        //check values
        $this->assertEquals($expected,$actual);
    }
}