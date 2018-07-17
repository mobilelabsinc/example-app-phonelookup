//
//  ProductItem.h
//  Phone Lookup
//
//  Created by Steve Orlando on 9/7/13.
//  Copyright (c) 2013 Mobile Labs. All rights reserved.
//

#import <Foundation/Foundation.h>


@interface ProductItem : NSObject {
    NSString *ProductID;
    NSString *ProductName;
	NSString *Manufacturer;
	NSString *Description;
	NSString *Price;
	NSString *InStock;
	NSString *OperatingSystem;
    NSString *QtyOnHand;
    NSString *Carrier;
}
@property (nonatomic, retain) NSString *ProductID;
@property (nonatomic, retain) NSString *ProductName;
@property (nonatomic, retain) NSString *Manufacturer;
@property (nonatomic, retain) NSString *Description;
@property (nonatomic, retain) NSString *Price;
@property (nonatomic, retain) NSString *InStock;
@property (nonatomic, retain) NSString *OperatingSystem;
@property (nonatomic, retain) NSString *QtyOnHand;
@property (nonatomic, retain) NSString *Carrier;
@end
