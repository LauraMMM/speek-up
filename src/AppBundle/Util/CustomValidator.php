<?php
/**
 * Created by PhpStorm.
 * User: aiftemi
 * Date: 21/11/2015
 * Time: 7:56 PM
 */

namespace AppBundle\Util;


class CustomValidator
{

    /**
     * @param $arrayOfVars
     * @return response array for json page or empty if everything is ok
     */
    public static function validateEmptyVarsForResponse($arrayOfVars)
    {
        //final response which goes out empty if everything ok
        $response = array();

        //error messages if any found
        $messages = array();

        //test each input set of var name var value for validation
        foreach($arrayOfVars as $varName => $varValue)
        {
            //if value of a var is empty
            if (empty($varValue))
            {
                //add error message in array
                $messages[] = "Invalid param $varName";
            }
        }

        //if any empty var found
        if (!empty($messages))
        {
            $response["status"] = "0";
            $response["messages"] = $messages;
        }

        return $response;
    }

    /**
     * Retrieve on record in array and format the response for output as well. Make sure the object has fb id
     * @param $doctrineMongo Database connection
     * @param $collection Table name
     * @param $collectionId Record id
     * @return array
     */
    public static function validateObjectForService($doctrineMongo,$collection,$collectionId)
    {
        //init standard response format
        $response = array();

        //get repository (table/collection)
        $objectRepository = $doctrineMongo->getRepository('AcmeSpeekUpMongoBundle:'.$collection);

        //identify object in our database
        $recordObject = $objectRepository->findOneByFbId($collectionId);

        //test user existence
        if (!$recordObject)
        {
            //if user does not exists return standard error response
            $response["status"] = "0";
            $response["messages"] = array("Unknown record in table ".$collection);

        }
        else
        {
            $response["status"] = "1";
            $response["data"] = $recordObject;
        }
        return $response;
    }
}