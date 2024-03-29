<?php
/**
 * Created by PhpStorm.
 * User: aiftemi
 * Date: 22/11/2015
 * Time: 12:16 AM
 */

namespace Acme\SpeekUpMongoBundle\Document;

use Doctrine\ODM\MongoDB\Mapping\Annotations as MongoDB;

/**
 * @MongoDB\Document
 */
class Activity
{
    /**
     * @MongoDB\Id
     */
    protected $id;

    /**
     * @MongoDB\String @MongoDB\Index(unique=false, order="asc")
     */
    protected $eventId;

    /**
     * @MongoDB\String
     */
    protected $userId;

    /**
     * @MongoDB\String
     */
    protected $type;

    /**
     * @MongoDB\String
     */
    protected $status;

    /**
     * @MongoDB\Int
     */
    protected $expireAt;

    /**
     * @MongoDB\Int
     */
    protected $yesCount;

    /**
     * @MongoDB\Int
     */
    protected $noCount;

    /**
     * @MongoDB\String
     */
    protected $text;

    /**
     * @MongoDB\Collection
     */
    protected $voters = array();

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
     * Set eventId
     *
     * @param string $eventId
     * @return self
     */
    public function setEventId($eventId)
    {
        $this->eventId = $eventId;
        return $this;
    }

    /**
     * Get eventId
     *
     * @return string $eventId
     */
    public function getEventId()
    {
        return $this->eventId;
    }

    /**
     * Set type
     *
     * @param string $type
     * @return self
     */
    public function setType($type)
    {
        $this->type = $type;
        return $this;
    }

    /**
     * Get type
     *
     * @return string $type
     */
    public function getType()
    {
        return $this->type;
    }

    /**
     * Set status
     *
     * @param string $status
     * @return self
     */
    public function setStatus($status)
    {
        $this->status = $status;
        return $this;
    }

    /**
     * Get status
     *
     * @return string $status
     */
    public function getStatus()
    {
        return $this->status;
    }

    /**
     * Set expireAt
     *
     * @param int $expireAt
     * @return self
     */
    public function setExpireAt($expireAt)
    {
        $this->expireAt = $expireAt;
        return $this;
    }

    /**
     * Get expireAt
     *
     * @return int $expireAt
     */
    public function getExpireAt()
    {
        return $this->expireAt;
    }

    /**
     * Set yesCount
     *
     * @param int $yesCount
     * @return self
     */
    public function setYesCount($yesCount)
    {
        $this->yesCount = $yesCount;
        return $this;
    }

    /**
     * Get yesCount
     *
     * @return int $yesCount
     */
    public function getYesCount()
    {
        return $this->yesCount;
    }

    /**
     * Set noCount
     *
     * @param int $noCount
     * @return self
     */
    public function setNoCount($noCount)
    {
        $this->noCount = $noCount;
        return $this;
    }

    /**
     * Get noCount
     *
     * @return int $noCount
     */
    public function getNoCount()
    {
        return $this->noCount;
    }

    /**
     * Set text
     *
     * @param string $text
     * @return self
     */
    public function setText($text)
    {
        $this->text = $text;
        return $this;
    }

    /**
     * Get text
     *
     * @return string $text
     */
    public function getText()
    {
        return $this->text;
    }

    /**
     * Set voters
     *
     * @param collection $voters
     * @return self
     */
    public function setVoters($voters)
    {
        $this->voters = $voters;
        return $this;
    }

    /**
     * Get voters
     *
     * @return collection $voters
     */
    public function getVoters()
    {
        return $this->voters;
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
     * Is expired
     *
     * @return boolean
     */
    public function isExpired()
    {
        if ($this->getExpireAt() < time())
        {
            return true;
        }
        else
        {
            return false;
        }
    }

    /**
     * @return array value of this object
     */
    public function toArray()
    {
        return array(
            "id" => $this->id,
            "eventId" => $this->eventId,
            "userId" => $this->userId,
            "type" => $this->type,
            "status" => $this->status,
            "expireAt" => $this->expireAt,
            "expireAtFormatted" => date ( "Y-m-d H:i:s", $this->expireAt ),
            "currentTime" => time(),
            "currentTimeFormatted" => date ( "Y-m-d H:i:s", time() ),
            "yesCount" => $this->yesCount,
            "noCount" => $this->noCount,
            "text" => $this->text,
            "voters" => $this->voters,
            "expired" => $this->isExpired(),

        );
    }
}
