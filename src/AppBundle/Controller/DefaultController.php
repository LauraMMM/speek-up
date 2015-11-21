<?php
namespace AppBundle\Controller;

use Acme\SpeekUpMongoBundle\Document\User;
use Sensio\Bundle\FrameworkExtraBundle\Configuration\Route;
use Symfony\Bundle\FrameworkBundle\Controller\Controller;
use Symfony\Component\HttpFoundation\Request;
use Symfony\Component\HttpFoundation\Response;
use Symfony\Component\HttpFoundation\JsonResponse;

class DefaultController extends Controller
{
    /**
     * @Route("/", name="index")
     */
    public function indexAction(Request $request)
    {
        return new JsonResponse(array("ok"=>1));
    }

    /**
     * @Route("/mongo", name="mongo")
     */
    public function mongoAction(Request $request)
    {
        $dm = $this->get('doctrine_mongodb')->getManager();
        $dm->getSchemaManager()->ensureIndexes();


        $dm = $this->get('doctrine_mongodb')->getManager();
        $dm->getSchemaManager()->ensureIndexes();

        $dm->flush();

        return new JsonResponse(array("updateDb" => 1));
    }
}
