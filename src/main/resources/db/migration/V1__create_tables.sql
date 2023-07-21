CREATE TABLE IF NOT EXISTS `user_details` (
    `id` VARCHAR(50) NOT NULL,
    `calling_code` VARCHAR(10) NOT NULL,
    `phone_number` VARCHAR(20) UNIQUE NOT NULL,
    `first_name` VARCHAR(100) NOT NULL,
    `last_name` VARCHAR(100) NOT NULL,
    `description` TEXT,
    `profile_picture_url` TEXT,

    PRIMARY KEY (`id`)
);

CREATE TABLE IF NOT EXISTS `users` (
    `id` VARCHAR(50) NOT NULL,
    `email` VARCHAR(100) UNIQUE NOT NULL,
    `password` VARCHAR(100) NOT NULL,
    `role` VARCHAR(50) NOT NULL,
    `fk_user_details` VARCHAR(50) NOT NULL,

    PRIMARY KEY (`id`)
);

CREATE TABLE IF NOT EXISTS `associations` (
    `id` VARCHAR(50) NOT NULL,
    `country` VARCHAR(100) NOT NULL,
    `locality` VARCHAR(100) NOT NULL,
    `administrative_area` VARCHAR(50) NOT NULL,
    `zip_code` VARCHAR(50) NOT NULL,
    `street` VARCHAR(50) NOT NULL,
    `number` VARCHAR(50) NOT NULL,
    `staircase` VARCHAR(50) NOT NULL,
    `latitude` DOUBLE NOT NULL,
    `longitude` DOUBLE NOT NULL,

    PRIMARY KEY (`id`)
);

CREATE TABLE IF NOT EXISTS `apartments` (
    `id` VARCHAR(50) NOT NULL,
    `number` VARCHAR(50) NOT NULL,
    `number_of_persons` INT NOT NULL,
    `code` VARCHAR(100) NOT NULL,
    `surface` DOUBLE NOT NULL,
    `association_id` VARCHAR(50) NOT NULL,

    PRIMARY KEY (`id`),
    FOREIGN KEY (`association_id`) REFERENCES `associations`(`id`)
);

CREATE TABLE IF NOT EXISTS `association_members` (
    `id` VARCHAR(50) NOT NULL,
    `member_id` VARCHAR(50) NOT NULL,
    `association_id` VARCHAR(50) NOT NULL,
    `apartment_id` VARCHAR(50),
    `role` VARCHAR(30),

    PRIMARY KEY (`id`),
    FOREIGN KEY (`member_id`) REFERENCES `users`(`id`),
    FOREIGN KEY (`association_id`) REFERENCES `associations`(`id`),
    FOREIGN KEY (`apartment_id`) REFERENCES `apartments`(`id`)
);

CREATE TABLE IF NOT EXISTS `invoices` (
    `id` VARCHAR(50) NOT NULL,
    `document_url` TEXT NOT NULL,
    `month` INT NOT NULL,
    `year` INT NOT NULL,
    `name` VARCHAR(50) NOT NULL,
    `number` VARCHAR(50) NOT NULL,
    `amount` DOUBLE NOT NULL,
    `type` VARCHAR(50) NOT NULL,
    `method` VARCHAR(50) NOT NULL,
    `association_id` VARCHAR(50) NOT NULL,

    PRIMARY KEY (`id`),
    FOREIGN KEY (`association_id`) REFERENCES `associations`(`id`)
);

CREATE TABLE IF NOT EXISTS `indexes` (
    `id` VARCHAR(50) NOT NULL,
    `old_index` DOUBLE NOT NULL,
    `new_index` DOUBLE NOT NULL,
    `type` VARCHAR(50) NOT NULL,
    `month` INT NOT NULL,
    `year` INT NOT NULL,
    `apartment_id` VARCHAR(50) NOT NULL,

    PRIMARY KEY (`id`),
    FOREIGN KEY (`apartment_id`) REFERENCES `apartments`(`id`)
);

CREATE TABLE IF NOT EXISTS `invoices_distribution` (
    `id` VARCHAR(50) NOT NULL,
    `amount` DOUBLE NOT NULL,
    `is_paid` BOOLEAN NOT NULL,
    `apartment_id` VARCHAR(50) NOT NULL,
    `invoice_id` VARCHAR(50) NOT NULL,

    PRIMARY KEY (`id`),
    FOREIGN KEY (`apartment_id`) REFERENCES `apartments`(`id`),
    FOREIGN KEY (`invoice_id`) REFERENCES `invoices`(`id`)
);