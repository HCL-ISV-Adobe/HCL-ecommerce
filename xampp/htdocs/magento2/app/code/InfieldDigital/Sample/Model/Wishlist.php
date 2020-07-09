<?php
/**
 * @copyright: Copyright © 2017 mediaman GmbH. All rights reserved.
 * @see LICENSE.txt
 */

namespace InfieldDigital\Sample\Model;

use InfieldDigital\Sample\Api\WishlistInterface;

/**
 * Class Wishlist
 * @package InfieldDigital\Sample\Model
 */
class Wishlist extends \Magento\Wishlist\Model\Wishlist implements WishlistInterface
{

    /**
     * @inheritdoc
     */
    public function getItems()
    {
        return $this->getItemCollection()->getItems();
    }
}
