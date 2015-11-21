<?php

namespace Acme\SpeekUpMongoBundle\Controller;

use Symfony\Bundle\FrameworkBundle\Controller\Controller;

class DefaultController extends Controller
{
    public function indexAction($name)
    {
        return $this->render('AcmeSpeekUpMongoBundle:Default:index.html.twig', array('name' => $name));
    }
}
