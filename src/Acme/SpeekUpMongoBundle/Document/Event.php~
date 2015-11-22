<?php
/**
 * Created by PhpStorm.
 * User: aiftemi
 * Date: 21/11/2015
 * Time: 4:16 PM
 */

namespace Acme\SpeekUpMongoBundle\Document;

use Doctrine\ODM\MongoDB\Mapping\Annotations as MongoDB;

/**
 * @MongoDB\Document
 */
class Event
{
    /**
     * @MongoDB\Id
     */
    protected $id;

    /**
     * @MongoDB\String @MongoDB\Index(unique=false, order="asc")
     */
    protected $userId;

    /**
     * @MongoDB\String @MongoDB\Index(unique=true, order="asc")
     */
    protected $fbId;

    /**
     * @MongoDB\String
     */
    protected $fbTitle;

    /**
     * @MongoDB\String
     */
    protected $fbDescription;

    /**
     * @MongoDB\String
     */
    protected $fbAvatar;

    /**
     * @MongoDB\String
     */
    protected $fbCover;

    /**
     * @MongoDB\Int
     */
    protected $fbStartTime;

    /**
     * @MongoDB\Int
     */
    protected $fbEndTime;

    /**
     * @MongoDB\String
     */
    protected $fbLocationName;

    /**
     * @MongoDB\String
     */
    protected $fbLatitude;

    /**
     * @MongoDB\String
     */
    protected $fbLongitude;

    /**
     * @MongoDB\Int
     */
    protected $fbCount;

    /**
     * @MongoDB\Int
     */
    protected $localCount;

    /**
     * @MongoDB\Collection
     */
    protected $attendees = array();

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
     * Set fbTitle
     *
     * @param string $fbTitle
     * @return self
     */
    public function setFbTitle($fbTitle)
    {
        $this->fbTitle = $fbTitle;
        return $this;
    }

    /**
     * Get fbTitle
     *
     * @return string $fbTitle
     */
    public function getFbTitle()
    {
        return $this->fbTitle;
    }

    /**
     * Set fbDescription
     *
     * @param string $fbDescription
     * @return self
     */
    public function setFbDescription($fbDescription)
    {
        $this->fbDescription = $fbDescription;
        return $this;
    }

    /**
     * Get fbDescription
     *
     * @return string $fbDescription
     */
    public function getFbDescription()
    {
        return $this->fbDescription;
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
     * Set fbCover
     *
     * @param string $fbCover
     * @return self
     */
    public function setFbCover($fbCover)
    {
        $this->fbCover = $fbCover;
        return $this;
    }

    /**
     * Get fbCover
     *
     * @return string $fbCover
     */
    public function getFbCover()
    {
        return $this->fbCover;
    }

    /**
     * Set fbStartTime
     *
     * @param string $fbStartTime
     * @return self
     */
    public function setFbStartTime($fbStartTime)
    {
        $this->fbStartTime = $fbStartTime;
        return $this;
    }

    /**
     * Get fbStartTime
     *
     * @return string $fbStartTime
     */
    public function getFbStartTime()
    {
        return $this->fbStartTime;
    }

    /**
     * Set fbEndTime
     *
     * @param string $fbEndTime
     * @return self
     */
    public function setFbEndTime($fbEndTime)
    {
        $this->fbEndTime = $fbEndTime;
        return $this;
    }

    /**
     * Get fbEndTime
     *
     * @return string $fbEndTime
     */
    public function getFbEndTime()
    {
        return $this->fbEndTime;
    }

    /**
     * Set fbLocationName
     *
     * @param string $fbLocationName
     * @return self
     */
    public function setFbLocationName($fbLocationName)
    {
        $this->fbLocationName = $fbLocationName;
        return $this;
    }

    /**
     * Get fbLocationName
     *
     * @return string $fbLocationName
     */
    public function getFbLocationName()
    {
        return $this->fbLocationName;
    }

    /**
     * Set fbLatitude
     *
     * @param string $fbLatitude
     * @return self
     */
    public function setFbLatitude($fbLatitude)
    {
        $this->fbLatitude = $fbLatitude;
        return $this;
    }

    /**
     * Get fbLatitude
     *
     * @return string $fbLatitude
     */
    public function getFbLatitude()
    {
        return $this->fbLatitude;
    }

    /**
     * Set fbLongitude
     *
     * @param string $fbLongitude
     * @return self
     */
    public function setFbLongitude($fbLongitude)
    {
        $this->fbLongitude = $fbLongitude;
        return $this;
    }

    /**
     * Get fbLongitude
     *
     * @return string $fbLongitude
     */
    public function getFbLongitude()
    {
        return $this->fbLongitude;
    }

    /**
     * Set fbCount
     *
     * @param int $fbCount
     * @return self
     */
    public function setFbCount($fbCount)
    {
        $this->fbCount = $fbCount;
        return $this;
    }

    /**
     * Get fbCount
     *
     * @return int $fbCount
     */
    public function getFbCount()
    {
        return $this->fbCount;
    }

    /**
     * Set localCount
     *
     * @param int $localCount
     * @return self
     */
    public function setLocalCount($localCount)
    {
        $this->localCount = $localCount;
        return $this;
    }

    /**
     * Get localCount
     *
     * @return int $localCount
     */
    public function getLocalCount()
    {
        return $this->localCount;
    }

    /**
     * Set userId
     *
     * @param string $userId
     * @return self
     */
    public function setUserId($userId)
    {
        $this->userId = $userId;
        return $this;
    }

    /**
     * Get userId
     *
     * @return string $userId
     */
    public function getUserId()
    {
        return $this->userId;
    }

    /**
     * Set attendees
     *
     * @param hash $attendees
     * @return self
     */
    public function setAttendees($attendees)
    {
        $this->attendees = $attendees;
        return $this;
    }

    /**
     * Get attendees
     *
     * @return hash $attendees
     */
    public function getAttendees()
    {
        return $this->attendees;
    }

    /**
     * @return array value of this object
     */
    public function toArray()
    {
        return array(
            "id" => $this->fbId,
            "userId" => $this->userId,
            "fbTitle" => $this->fbTitle,
            "fbDescription" => $this->fbDescription,
            "fbAvatar" => $this->fbAvatar,
            "fbCover" => $this->fbCover,
            "fbStartTime" => $this->fbStartTime,
            "fbEndTime" => $this->fbEndTime,
            "fbLocationName" => $this->fbLocationName,
            "fbLatitude" => $this->fbLatitude,
            "fbLongitude" => $this->fbLongitude,
            "fbCount" => $this->fbCount,
            "localCount" => $this->localCount,
            "attendees" => $this->attendees,
        );
    }
}
