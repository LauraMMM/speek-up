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

        // find all objects
        $objects = $collectionRepository->findAll();
        foreach ($objects as $objectFound)
        {
            $responseArray[] = $objectFound->toArray();
        }

        //return all objects as array
        return $responseArray;
    }

    /**
     * Retrieve all records of collection in array format using filter. Make sure the object has to array implemented TODO
     * @param $doctrineMongo Database connection
     * @param $collection Table name
     * @param $searchParam Search parameters
     * @return array
     */
    public static function getCollectionObjectsAsArray($doctrineMongo,$collection,$searchParam)
    {
        //get collection repository (table/collection)
        $collectionRepository = $doctrineMongo->getRepository('AcmeSpeekUpMongoBundle:'.$collection);

        //init response objects list
        $responseArray = array();

        // find all objects
        $objects = $collectionRepository->findBy($searchParam);
        foreach ($objects as $objectFound)
        {
            $responseArray[] = $objectFound->toArray();
        }

        //return all objects as array
        return $responseArray;
    }

    public static function deleteCollectionObjectById($doctrineMongo,$collection,$collectionId)
    {
        //get collection repository (table/collection)
        $collectionRepository = $doctrineMongo->getRepository('AcmeSpeekUpMongoBundle:'.$collection);

        // find object
        $object = $collectionRepository->findOneById($collectionId);
        if ($object)
        {
            //delete the object from database
            $doctrineMongo->getManager()->remove($object);
        }

        //commit the delete
        $doctrineMongo->getManager()->flush();

        //do not return anything this time - just do it without write concern
    }

}