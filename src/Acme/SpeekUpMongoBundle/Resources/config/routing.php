<?php

use Symfony\Component\Routing\RouteCollection;
use Symfony\Component\Routing\Route;

$collection = new RouteCollection();

$collection->add('acme_speek_up_mongo_homepage', new Route('/hello/{name}', array(
    '_controller' => 'AcmeSpeekUpMongoBundle:Default:index',
)));

return $collection;
