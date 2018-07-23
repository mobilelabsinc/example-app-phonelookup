//
//  ProductViewController.m
//  Phone Lookup
//
//  Created by Steve Orlando on 9/7/13.
//  Copyright (c) 2013 Mobile Labs. All rights reserved.
//

#import "ProductViewController.h"
#import "QuartzCore/QuartzCore.h"
#import "PhoneLookup-Swift.h"



@implementation ProductViewController
@synthesize currentItem;

- (id)initWithNibName:(NSString *)nibNameOrNil bundle:(NSBundle *)nibBundleOrNil
{
    self = [super initWithNibName:nibNameOrNil bundle:nibBundleOrNil];
    if (self) {
        // Custom initialization
    }
    return self;
}


- (void)didReceiveMemoryWarning
{
    // Releases the view if it doesn't have a superview.
    [super didReceiveMemoryWarning];
    
    // Release any cached data, images, etc that aren't in use.
}

#pragma mark - View lifecycle

- (void)viewDidLoad
{
    [super viewDidLoad];
    CGRect frame = CGRectMake(10.0, 300.0, self.view.bounds.size.width-10, 1000.0); 
    
    NSStringDrawingOptions options = NSStringDrawingTruncatesLastVisibleLine | NSStringDrawingUsesLineFragmentOrigin;
    NSDictionary *attributDict = @{NSFontAttributeName: [UIFont fontWithName:@"Helvetica-Bold" size: 14.0]};
    
    CGRect calcSize = [currentItem.productDescription boundingRectWithSize:frame.size options:options attributes:attributDict context:nil];
    
    frame.size = calcSize.size;
    frame.size.height += 10;
    productDesc.frame=frame;
    
    UIImage *image;
    if([currentItem.operatingSystem rangeOfString:@"iOS"].location != NSNotFound)
        image=[UIImage imageNamed:@"Apple.png"];
    if([currentItem.operatingSystem rangeOfString:@"BlackBerry"].location != NSNotFound)
        image=[UIImage imageNamed:@"Blackberry.png"];
    if([currentItem.operatingSystem rangeOfString:@"Android"].location != NSNotFound)
        image=[UIImage imageNamed:@"Android.png"];
    if([currentItem.operatingSystem rangeOfString:@"Windows"].location != NSNotFound)
        image=[UIImage imageNamed:@"Windows.png"];
    
    imageView.image = image;    
    productName.text=currentItem.productName;
    ProductID.text=[NSString stringWithFormat:@"Product SKU # %@",currentItem.productID];
    productDesc.text=currentItem.productDescription;
    productPrice.text=[NSString stringWithFormat:@"Price : %@/Each",currentItem.price];
    productBrand.text=[NSString stringWithFormat:@"Manufacturer : %@",currentItem.manufacturer];
    if([currentItem.qtyOnHand intValue]==0)
        QtyOnHand.text=[NSString stringWithFormat:@"Online Quantity : Out of Stock"];
    else
        QtyOnHand.text=[NSString stringWithFormat:@"Online Quantity : %@",currentItem.qtyOnHand];
    //Carrier.text=[NSString stringWithFormat:@"Carrier # %@",currentItem.Carrier];
    OperatingSystem.text=[NSString stringWithFormat:@"Operating System : %@",currentItem.operatingSystem];
    
    
    productDesc.layer.borderWidth = 2.0f;
    productDesc.layer.cornerRadius = 8;
    productDesc.layer.borderColor = [[UIColor whiteColor] CGColor];
    
    imageView.layer.masksToBounds = YES;
    imageView.layer.cornerRadius = 10.0;
    
    scrollView.contentSize = CGSizeMake(self.view.frame.size.width,self.view.frame.size.height+frame.size.height-150);
    
  //  productBrand.frame=CGRectMake(144,310+frame.size.height +10,50,25);
  //  lblBrand.frame=CGRectMake(17,310+frame.size.height +10,50,25);
    
}

@end
