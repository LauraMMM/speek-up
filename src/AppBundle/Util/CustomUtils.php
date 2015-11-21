<?php
/**
 * Created by PhpStorm.
 * User: aiftemi
 * Date: 22/11/2015
 * Time: 1:05 AM
 */

namespace AppBundle\Util;


class CustomUtils
{
    /**
     * Retrieve all records of collection in array format. Make sure the object has to array implemented TODO
     * @param $doctrineMongo Database connection
     * @param $collection Table name
     * @return array
     */
    public static function getAllCollectionObjectsAsArray($doctrineMongo,$collection)
    {
        //get collection repository (table/collection)
        $collectionRepository = $doctrineMongo->getRepository('AcmeSpeekUpMongoBundle:'.$collection);

        //init response objects list
        $responseArray = array();

        // find all events
        $objects = $collectionRepository->findAll();
        foreach ($objects as $objectFound)
        {
            $responseArray[] = $objectFound->toArray();
        }

        //return all objects as array
        return $responseArray;
    }
}