<?php
/**
 * Created by PhpStorm.
 * User: aiftemi
 * Date: 21/11/2015
 * Time: 3:17 PM
 */

namespace Acme\SpeekUpMongoBundle\Document;

use Doctrine\ODM\MongoDB\Mapping\Annotations as MongoDB;

/**
 * @MongoDB\Document
 */
class User
{
    /**
     * @MongoDB\Id
     */
    protected $id;

    /**
     * @MongoDB\String @MongoDB\Index(unique=true, order="asc")
     */
    protected $fbId;

    /**
     * @MongoDB\String
     */
    protected $fbName;

    /**
     * @MongoDB\String
     */
    protected $fbAvatar;

    /**
     * @MongoDB\String
     */
    protected $deviceId;

    /**
     * @MongoDB\Collection
     */
    protected $events = array();

    /**
     * Get id
     *
     * @return id $id
     */
    public function getId()
    {
        return $this->id;
    }

    /**
     * Set fbId
     *
     * @param string $fbId
     * @return self
     */
    public function setFbId($fbId)
    {
        $this->fbId = $fbId;
        return $this;
    }

    /**
     * Get fbId
     *
     * @return string $fbId
     */
    public function getFbId()
    {
        return $this->fbId;
    }

    /**
     * Set fbName
     *
     * @param string $fbName
     * @return self
     */
    public function setFbName($fbName)
    {
        $this->fbName = $fbName;
        return $this;
    }

    /**
     * Get fbName
     *
     * @return string $fbName
     */
    public function getFbName()
    {
        return $this->fbName;
    }

    /**
     * Set fbAvatar
     *
     * @param string $fbAvatar
     * @return self
     */
    public function setFbAvatar($fbAvatar)
    {
        $this->fbAvatar = $fbAvatar;
        return $this;
    }

    /**
     * Get fbAvatar
     *
     * @return string $fbAvatar
     */
    public function getFbAvatar()
    {
        return $this->fbAvatar;
    }

    /**
     * Set deviceId
     *
     * @param string $deviceId
     * @return self
     */
    public function setDeviceId($deviceId)
    {
        $this->deviceId = $deviceId;
        return $this;
    }

    /**
     * Get deviceId
     *
     * @return string $deviceId
     */
    public function getDeviceId()
    {
        return $this->deviceId;
    }

    /**
     * Set events
     *
     * @param hash $events
     * @return self
     */
    public function setEvents($events)
    {
        $this->events = $events;
        return $this;
    }

    /**
     * Get events
     *
     * @return hash $events
     */
    public function getEvents()
    {
        return $this->events;
    }
}
