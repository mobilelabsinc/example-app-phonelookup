//
//  ProductViewController.h
//  Phone Lookup
//
//  Created by Steve Orlando on 9/7/13.
//  Copyright (c) 2013 Mobile Labs. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "PhoneLookup-Swift.h"

@interface ProductViewController : UIViewController <UIScrollViewDelegate> {
    Item *currentItem;
    IBOutlet UILabel *productName;
    IBOutlet UILabel *ProductID;
    IBOutlet UILabel *OperatingSystem;
    IBOutlet UILabel *QtyOnHand;
    IBOutlet UILabel *Carrier;
    IBOutlet UITextView *productDesc;
    IBOutlet UILabel *productPrice;
    IBOutlet UILabel *productBrand;
    IBOutlet UIScrollView *scrollView;
    IBOutlet UILabel *lblBrand;
    IBOutlet UIImageView *imageView;
}
@property (nonatomic,retain) Item *currentItem;
@end
