//
//  SearchListViewController.m
//  demo
//
//  Created by Steve Orlando on 9/7/13.
//  Copyright (c) 2013 Mobile Labs. All rights reserved.
//

#import "SearchListViewController.h"
#import "MockModeController.h"
#import "ProductXMLHandler.h"
#import "ProductItem.h"
#import "ProductViewController.h"
#import <QuartzCore/QuartzCore.h>

@interface UIImage (scale)
- (UIImage*)imageScaledToSize:(CGSize)size;
@end

@implementation UIImage(scale)
- (UIImage*)imageScaledToSize:(CGSize)size
{
    UIGraphicsBeginImageContext(size);
    [self drawInRect:CGRectMake(0, 0, size.width, size.height)];
    UIImage *image = UIGraphicsGetImageFromCurrentImageContext();
    UIGraphicsEndImageContext();
    return image;
}
@end

static SearchListViewController *instance;

@interface SearchListViewController ()

@end

@implementation SearchListViewController
@synthesize productArray,proTableView,productItem;

+ (SearchListViewController *)currentInstance {
    return instance;
}

- (id)initWithNibName:(NSString *)nibNameOrNil bundle:(NSBundle *)nibBundleOrNil
{
    self = [super initWithNibName:nibNameOrNil bundle:nibBundleOrNil];
    if (self) {
        // Custom initialization
    }
    return self;
}

- (void)viewDidLoad
{
    instance = self;
    [super viewDidLoad];
    // Do any additional setup after loading the view from its nib.
}

- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}



- (NSInteger)numberOfSectionsInTableView:(UITableView *)tableView {
    // Return the number of sections.
    return 1;
}


- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section {
    // Return the number of rows in the section.
    return [productArray count];
}

- (CGFloat)tableView:(UITableView *)tableView heightForRowAtIndexPath:(NSIndexPath *)indexPath
{
	return 80.0 ;
}

// Customize the appearance of table view cells.
- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath {
    
    static NSString *CellIdentifier = @"Cell";
    UITableViewCell *cell = [tableView dequeueReusableCellWithIdentifier:CellIdentifier];
	if (cell == nil)
	{
		cell = [[UITableViewCell alloc] initWithStyle:UITableViewCellStyleSubtitle reuseIdentifier:CellIdentifier];
		cell.accessoryType = UITableViewCellAccessoryDisclosureIndicator;
	}
    else
    {
		UILabel *textLabel = (UILabel *) [cell viewWithTag:1];
		UILabel *Instock = (UILabel *) [cell viewWithTag:2];
		UILabel *detailTextLabel = (UILabel *) [cell viewWithTag:3];
		UILabel *detailSubTextLabel = (UILabel *) [cell viewWithTag:4];
		UILabel *priceTextLabel = (UILabel *) [cell viewWithTag:5];
		UILabel *pricesupTextLabel = (UILabel *) [cell viewWithTag:6];
		UILabel *pricesubTextLabel = (UILabel *) [cell viewWithTag:7];
		[textLabel removeFromSuperview];
		[Instock removeFromSuperview];
		[detailTextLabel removeFromSuperview];
		[detailSubTextLabel removeFromSuperview];
		[priceTextLabel removeFromSuperview];
		[pricesupTextLabel removeFromSuperview];
		[pricesubTextLabel removeFromSuperview];
	}
	
    NSInteger i = indexPath.row;
    ProductItem *desc=(ProductItem *)[productArray objectAtIndex:i];
	
    UILabel *textLabel = [[UILabel alloc] initWithFrame:CGRectMake(70.0f, 0.0f, 100.0f, 30.0f)];
    textLabel.text = desc.ProductName;
    textLabel.tag =1;
    textLabel.textAlignment =NSTextAlignmentLeft;
    textLabel.textColor = [UIColor colorWithRed:0/255.0 green:143/255.0 blue:213/255.0 alpha:1.0];
    textLabel.font = [UIFont fontWithName:@"Helvetica-Bold" size: 14.0];
    textLabel.lineBreakMode = NSLineBreakByWordWrapping;
    textLabel.numberOfLines = 1;
    textLabel.backgroundColor = [UIColor clearColor];
    [cell.contentView addSubview:textLabel];
    
    UILabel *Instock = [[UILabel alloc] initWithFrame:CGRectMake(160.0f, 0.0f, 100.0f, 30.0f)];
    
    Instock.tag =2;
    Instock.textAlignment =NSTextAlignmentLeft;
    if([desc.QtyOnHand intValue]==0){
        Instock.textColor = [UIColor redColor];
        Instock.text = @"[Out of Stock]";
    }else{
        Instock.textColor = [UIColor colorWithRed:0 green:0.7  blue:0.3 alpha:1];
        Instock.text = @"[In Stock]";
    }
    Instock.font = [UIFont fontWithName:@"Helvetica-Bold" size: 14.0];
    Instock.lineBreakMode = NSLineBreakByWordWrapping;
    Instock.numberOfLines = 1;
    Instock.backgroundColor = [UIColor clearColor];
    [cell.contentView addSubview:Instock];
    
    UILabel *detailTextLabel = [[UILabel alloc] initWithFrame:CGRectMake(70.0f, 25.0f, 100.0f, 25.0f)];
    detailTextLabel.text = desc.ProductID;
    detailTextLabel.tag =3;
    detailTextLabel.textAlignment =NSTextAlignmentLeft;
    detailTextLabel.textColor = [UIColor grayColor];
    detailTextLabel.font = [UIFont fontWithName:@"Helvetica-Bold" size: 13.0];
    detailTextLabel.lineBreakMode = NSLineBreakByWordWrapping;
    detailTextLabel.numberOfLines = 1;
    detailTextLabel.backgroundColor = [UIColor clearColor];
    [cell.contentView addSubview:detailTextLabel];
    
    UILabel *detailSubTextLabel = [[UILabel alloc] initWithFrame:CGRectMake(70.0f, 50.0f, 100.0f, 25.0f)];
    detailSubTextLabel.text = [NSString stringWithFormat:@"Carrier : %@", desc.Carrier];
    detailSubTextLabel.tag =4;
    detailSubTextLabel.textAlignment =NSTextAlignmentLeft;
    detailSubTextLabel.textColor = [UIColor grayColor];
    detailSubTextLabel.font = [UIFont fontWithName:@"Helvetica-Bold" size: 12.0];
    detailSubTextLabel.lineBreakMode = NSLineBreakByWordWrapping;
    detailSubTextLabel.numberOfLines = 1;
    detailSubTextLabel.backgroundColor = [UIColor clearColor];
    [cell.contentView addSubview:detailSubTextLabel];
    
    NSArray *listItems = [desc.Price componentsSeparatedByString:@"."];
    UILabel *priceTextLabel = [[UILabel alloc] initWithFrame:CGRectMake(230.0f, 25.0f, 50.0f, 25.0f)];
    if([listItems count]>=1)
        priceTextLabel.text = [listItems objectAtIndex:0];
    else
        priceTextLabel.text = @"00";
    priceTextLabel.tag =5;
    priceTextLabel.textAlignment =NSTextAlignmentRight;
    priceTextLabel.textColor = [UIColor orangeColor];
    priceTextLabel.font = [UIFont fontWithName:@"Helvetica-Bold" size: 18.0];
    priceTextLabel.lineBreakMode = NSLineBreakByWordWrapping;
    priceTextLabel.numberOfLines = 1;
    priceTextLabel.backgroundColor = [UIColor clearColor];
    [cell.contentView addSubview:priceTextLabel];
    
    UILabel *pricesupTextLabel = [[UILabel alloc] initWithFrame:CGRectMake(280.0f, 20.0f, 20.0f, 25.0f)];
    if([listItems count]==2)
        pricesupTextLabel.text = [listItems objectAtIndex:1];
    else
        pricesupTextLabel.text = @"00";
    //pricesupTextLabel.text = [listItems objectAtIndex:1];
    pricesupTextLabel.tag =6;
    pricesupTextLabel.textAlignment =NSTextAlignmentLeft;
    pricesupTextLabel.textColor = [UIColor orangeColor];
    pricesupTextLabel.font = [UIFont fontWithName:@"Helvetica-Bold" size: 10.0];
    pricesupTextLabel.lineBreakMode = NSLineBreakByWordWrapping;
    pricesupTextLabel.numberOfLines = 1;
    pricesupTextLabel.backgroundColor = [UIColor clearColor];
    [cell.contentView addSubview:pricesupTextLabel];
    
    UILabel *pricesubTextLabel = [[UILabel alloc] initWithFrame:CGRectMake(280.0f, 30.0f, 20.0f, 25.0f)];
    pricesubTextLabel.text = @"EA";
    pricesubTextLabel.tag =7;
    pricesubTextLabel.textAlignment =NSTextAlignmentLeft;
    pricesubTextLabel.textColor = [UIColor orangeColor];
    pricesubTextLabel.font = [UIFont fontWithName:@"Helvetica-Bold" size: 10.0];
    pricesubTextLabel.lineBreakMode = NSLineBreakByWordWrapping;
    pricesubTextLabel.numberOfLines = 1;
    pricesubTextLabel.backgroundColor = [UIColor clearColor];
    [cell.contentView addSubview:pricesubTextLabel];
    
    cell.selectionStyle = UITableViewCellSelectionStyleNone;
    
    UIImage *image;
    if([desc.OperatingSystem rangeOfString:@"iOS"].location != NSNotFound)
        image=[UIImage imageNamed:@"Apple.png"];
    if([desc.OperatingSystem rangeOfString:@"BlackBerry"].location != NSNotFound)
        image=[UIImage imageNamed:@"Blackberry.png"];
    if([desc.OperatingSystem rangeOfString:@"Android"].location != NSNotFound)
        image=[UIImage imageNamed:@"Android.png"];
    if([desc.OperatingSystem rangeOfString:@"Windows"].location != NSNotFound)
        image=[UIImage imageNamed:@"Windows.png"];
    
    cell.imageView.image = [image imageScaledToSize:CGSizeMake(50, 50)];
    cell.imageView.layer.masksToBounds = YES;
    cell.imageView.layer.cornerRadius = 10.0;
	return cell;
}

- (void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath {
    // Navigation logic may go here. Create and push another view controller.
    
    ProductViewController *productViewController = [[ProductViewController alloc] initWithNibName:@"ProductViewController" bundle:nil];
    
    productViewController.title=@"Product";
	NSInteger i = indexPath.row;
    
	productViewController.currentItem=(ProductItem *)[productArray objectAtIndex:i];
	[self.navigationController pushViewController:productViewController animated:YES];
    
    
}

@end
