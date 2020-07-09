<?php
/**
 * @copyright: Copyright © 2017 mediaman GmbH. All rights reserved.
 * @see LICENSE.txt
 */

namespace InfieldDigital\Sample\Api;

use Magento\Catalog\Api\Data\ProductInterface;

/**
 * Interface ItemInterface
 * @package InfieldDigital\Sample\Api
 * @api
 */
interface ItemInterface
{

    /**
     * @return int
     */
    public function getId(): int;

    /**
     * @return \Magento\Catalog\Api\Data\ProductInterface
     */
    public function getProduct(): ProductInterface;
}
