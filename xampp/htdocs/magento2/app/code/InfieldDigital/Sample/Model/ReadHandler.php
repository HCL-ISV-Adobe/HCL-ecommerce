<?php
/**
 * @copyright: Copyright © 2017 mediaman GmbH. All rights reserved.
 * @see LICENSE.txt
 */

namespace InfieldDigital\Sample\Model;

use Magento\Catalog\Api\Data\ProductExtensionFactory;

class ReadHandler
{

        /**
         * @var \Magento\CatalogInventory\Api\StockRegistryInterface
         */
        private $stockRegistry;
        private $productExtensionFactory;

        /**
         * @param \Magento\CatalogInventory\Api\StockRegistryInterface $stockRegistry
         */
        public function __construct(
            \Magento\CatalogInventory\Api\StockRegistryInterface $stockRegistry,
            ProductExtensionFactory $productExtensionFactory
        ) {
            $this->stockRegistry = $stockRegistry;
            $this->productExtensionFactory = $productExtensionFactory;
        }

        /**
         * Add stock item information to the product's extension attributes
         *
         * @param \Magento\Catalog\Model\Product $product
         * @return \Magento\Catalog\Model\Product
         */
        public function afterLoad(\Magento\Catalog\Model\Product $product)
        {
            $productExtension = $product->getExtensionAttributes();
            $productExtension->setStockItem($this->stockRegistry->getStockItem($product->getId()));
            $product->setExtensionAttributes($productExtension);
            return $product;
        }

        public function afterGetList(
            \Magento\Catalog\Api\ProductRepositoryInterface $subject,
            \Magento\Framework\Api\SearchResults $searchResult
        ){
            /** @var \Magento\Catalog\Api\Data\ProductInterface $product */
            foreach ($searchResult->getItems() as $product) {
                $this->addStockToProduct($product);
            }
            return $searchResult;
        }

        public function afterGet
        (
            \Magento\Catalog\Api\ProductRepositoryInterface $subject,
            \Magento\Catalog\Api\Data\ProductInterface $product
        ) {
            $this->addStockToProduct($product);
            return $product;
        }

        /**
         * @param \Magento\Catalog\Api\Data\ProductInterface $product
         * @return self
         */
        private function addStockToProduct(\Magento\Catalog\Api\Data\ProductInterface $product)
        {
            $extensionAttributes = $product->getExtensionAttributes();
            if (empty($extensionAttributes)) {
                $extensionAttributes = $this->productExtensionFactory->create();
            }
            $stock = $this->stockRegistry->getStockItem($product->getId());

            $extensionAttributes->setStockItem($stock);
            $product->setExtensionAttributes($extensionAttributes);
            return $this;
        }

}