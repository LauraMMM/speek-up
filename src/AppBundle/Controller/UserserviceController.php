<?php
namespace AppBundle\Controller;

use Sensio\Bundle\FrameworkExtraBundle\Configuration\Route;
use Symfony\Bundle\FrameworkBundle\Controller\Controller;
use Symfony\Component\HttpFoundation\Request;
use Symfony\Component\HttpFoundation\JsonResponse;

use Acme\SpeekUpMongoBundle\Document\User;
use AppBundle\Util\CustomValidator;

class UserserviceController extends Controller
{
    /**
     * @Route("/userservice", name="userserviceindex")
     */
    public function indexAction(Request $request)
    {
        return new JsonResponse(array("serviceName"=>"userservice"));
    }

    /**
     * @Route("/userservice/login", name="userservicelogin")
     */
    public function loginAction(Request $request)
    {
        //get post data
        $fbId       = $request->request->get('fbId');
        $fbName     = $request->request->get('fbName');
        $fbAvatar   = $request->request->get('fbAvatar');
        $deviceId   = $request->request->get('deviceId');

        $logger = $this->get('logger');
        $logger->info('POST:fbId:'.$fbId);
        $logger->info('POST:fbName:'.$fbName);
        $logger->info('POST:fbAvatar:'.$fbAvatar);
        $logger->info('POST:deviceId:'.$deviceId);

        //simulate some validations :D
        $validationArray = array(
            "fbId" => $fbId,
            "fbName" => $fbName,
            "fbAvatar" => $fbAvatar,
            "deviceId" => $deviceId,
        );

        //validate page input
        $response = CustomValidator::validateEmptyVarsForResponse($validationArray);
        if (!empty($response)) return new JsonResponse($response);

        //init page response
        $response = array();

        //retrieve MongoDB object
        $doctrineMongo = $this->get('doctrine_mongodb');

        //identify user in the database
        $methodResponse = CustomValidator::validateObjectForService($doctrineMongo,"User",$fbId);

        //test if user is found
        if ($methodResponse["status"] == "1")
        {
            //if user found get the user from response data tag
            $currentUser = $methodResponse["data"];

            //prepare response for an updated user
            $response["status"] = "1";
            $response["messages"] = array("User updated");
            $response["data"] = array("userId" => $currentUser->getFbId());
        }
        else
        {
            //if user is not found save it in User table
            $currentUser = new User();
            $currentUser->setFbId($fbId);

            //prepare response for an inserted user
            $response["status"] = "1";
            $response["messages"] = array("User created");
            $response["data"] = array("userId" => $currentUser->getFbId());
        }

        //update this fields no matter if the user is created or updated
        $currentUser->setFbName($fbName);
        $currentUser->setFbAvatar($fbAvatar);
        $currentUser->setDeviceId($deviceId);

        //save user
        $doctrineMongo->getManager()->persist($currentUser);

        //commit changes
        $doctrineMongo->getManager()->flush();

        //render json response
        return new JsonResponse($response);
    }
}