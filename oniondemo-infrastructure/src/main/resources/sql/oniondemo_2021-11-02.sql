CREATE TABLE `order` (
  `id` bigint(20) unsigned NOT NULL,
  `account` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '租户ID',
  `order_no` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '订单号',
  `customer_id` bigint(20) unsigned NOT NULL COMMENT '客户ID',
  `amount` int(11) unsigned NOT NULL DEFAULT '0' COMMENT '总金额',
  `status` tinyint(3) unsigned NOT NULL DEFAULT '0' COMMENT '状态：1未付款、2已付款',
  `created_at` datetime NOT NULL COMMENT '创建时间',
  `created_by` bigint(20) NOT NULL COMMENT '创建人',
  `updated_at` datetime NOT NULL COMMENT '修改时间',
  `updated_by` bigint(20) NOT NULL COMMENT '修改人',
  `version` int(10) unsigned NOT NULL DEFAULT '0' COMMENT '乐观锁版本号',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

CREATE TABLE `order_item` (
  `id` bigint(20) unsigned NOT NULL,
  `account` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '租户ID',
  `order_id` bigint(20) unsigned NOT NULL COMMENT '订单ID',
  `product_id` bigint(20) unsigned NOT NULL COMMENT '商品ID',
  `unit_price` int(11) unsigned NOT NULL COMMENT '单价，单位分',
  `quantity` int(10) unsigned NOT NULL COMMENT '数量',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

CREATE TABLE `product` (
  `id` bigint(20) unsigned NOT NULL,
  `account` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '租户ID',
  `name` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '商品名称',
  `description` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '商品描述',
  `price` int(11) unsigned NOT NULL COMMENT '商品价格，单位为分',
  `status` tinyint(11) NOT NULL DEFAULT '0' COMMENT '状态：0未上架、1已上架',
  `created_at` datetime NOT NULL COMMENT '创建时间',
  `created_by` bigint(20) unsigned NOT NULL COMMENT '创建人',
  `updated_at` datetime NOT NULL COMMENT '更新时间',
  `updated_by` bigint(20) unsigned NOT NULL COMMENT '更新人',
  `version` int(11) unsigned NOT NULL DEFAULT '0' COMMENT '乐观锁版本号',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_account_name` (`account`,`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

CREATE TABLE `product_inventory` (
  `id` bigint(20) unsigned NOT NULL,
  `account` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '租户ID',
  `product_id` bigint(20) NOT NULL COMMENT '商品ID',
  `quantity` int(11) NOT NULL COMMENT '数量',
  `created_at` datetime NOT NULL COMMENT '创建时间',
  `created_by` bigint(20) NOT NULL COMMENT '创建人',
  `updated_at` datetime NOT NULL COMMENT '修改时间',
  `updated_by` bigint(20) NOT NULL COMMENT '修改人',
  `version` int(10) unsigned NOT NULL DEFAULT '0' COMMENT '乐观锁版本号',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

CREATE TABLE `product_statistics` (
  `id` bigint(20) unsigned NOT NULL,
  `account` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '租户ID',
  `product_id` bigint(20) unsigned NOT NULL COMMENT '商品ID',
  `paid_order_count` int(11) unsigned NOT NULL COMMENT '已付款订单数',
  `unpaid_order_count` int(11) unsigned NOT NULL COMMENT '未付款订单数',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;