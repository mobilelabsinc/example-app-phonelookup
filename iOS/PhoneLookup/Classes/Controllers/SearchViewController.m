//
//  SearchViewController.m
//  demo
//
//  Created by Steve Orlando on 9/7/13.
//  Copyright (c) 2013 Mobile Labs. All rights reserved.
//

#import "SearchViewController.h"
#import "SearchListViewController.h"
#import "LoginViewController.h"
#import "MockModeController.h"
#import "ProductXMLHandler.h"
#import "ProductItem.h"

static SearchViewController *instance;

@implementation SearchViewController

@synthesize navigation;

+ (SearchViewController *)currentInstance {
    return instance;
}

- (id)initWithNibName:(NSString *)nibNameOrNil bundle:(NSBundle *)nibBundleOrNil
{
    self = [super initWithNibName:nibNameOrNil bundle:nibBundleOrNil];
    if (self) {
        // Custom initialization
        self.title = @"Search";
        
        self.navigationItem.rightBarButtonItem = [[UIBarButtonItem alloc] initWithTitle:@"Logout" style:UIBarButtonItemStylePlain target:self action:@selector(logOutAction:)];

    }
    return self;
}

- (void)viewDidLoad
{
    instance = self;
    
    [super viewDidLoad];
    // Do any additional setup after loading the view from its nib.
    
    Brand = [[NSMutableArray alloc] init];
    
	NSData *productData = [MockModeController getMockResponse:@"ProductXml"];
	ProductXMLHandler *productXMLlHandler = [[ProductXMLHandler alloc] init];
	productXMLlHandler.currentData = [ProductXMLHandler encodeDataToData:productData];
	NSArray *productXMLArray = [productXMLlHandler parseXMLFileAtURL:nil parseError:nil];
    
    NSMutableArray *productArray = [[NSMutableArray alloc] init];
    [productArray addObjectsFromArray:productXMLArray];

    NSSortDescriptor *sortByFrequency = [[NSSortDescriptor alloc] initWithKey:@"Manufacturer" ascending:YES];
    
    NSArray *descriptors = [NSArray arrayWithObject:sortByFrequency];
    NSArray *sortedArray = [productArray sortedArrayUsingDescriptors:descriptors];
    NSString *tempProduct;
    
    for (ProductItem *productItem in sortedArray)
    {
        
        if([productItem.Manufacturer length])
        {
            if([Brand count] == 0)
            {
                [Brand addObject:productItem.Manufacturer];
                tempProduct = productItem.Manufacturer;
            }
            else if(![tempProduct isEqual:productItem.Manufacturer])
            {
                [Brand addObject:productItem.Manufacturer];
                tempProduct = productItem.Manufacturer;
            }
        }
    }
    
    
	[Brand addObject:@"Any"];
    [Brand sortUsingSelector:@selector(localizedCaseInsensitiveCompare:)];
    
    // Do any additional setup after loading the view from its nib.
    
    [self.itemTextField setText:@""];
    
    [self.manufacturerTextField setText:[NSString stringWithFormat:@"%@",[Brand objectAtIndex:1]]];
    [self.manufacturerTextField setInputAccessoryView:self.pickerToolbar];
    [self.manufacturerTextField setInputView:self.pickerView];
    [self.pickerView selectRow:1 inComponent:0 animated:YES];
    
    
    [self.androidSwitch setOn:NO];
    [self.blackberrySwitch setOn:NO];
    [self.iosSwitch setOn:YES];
    [self.windowsSwitch setOn:NO];

}

- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}


- (IBAction)doneEditing:(id)sender {
    [self.manufacturerTextField resignFirstResponder];
}

- (BOOL)textFieldShouldReturn:(UITextField *)textField
{
    [textField resignFirstResponder];
    return YES;
}

- (void)textFieldDidBeginEditing:(UITextField *)textField
{
    
}

- (void)textFieldDidEndEditing:(UITextField *)textField
{
    [textField resignFirstResponder];
}



-(void)logOutAction:(id)sender
{
    [self.parentViewController dismissViewControllerAnimated:YES completion:NULL];
}


- (IBAction)search:(id)sender
{
    
    if((self.androidSwitch.isOn == NO) && (self.blackberrySwitch.isOn == NO)
       && (self.iosSwitch.isOn == NO) && (self.windowsSwitch.isOn == NO))
    {
        
        UIAlertController *alert = [UIAlertController alertControllerWithTitle:@"Error!" message:@"Invalid Selection Criteria" preferredStyle:UIAlertControllerStyleAlert];
        [alert addAction:[UIAlertAction actionWithTitle:@"OK" style:UIAlertActionStyleDefault handler:nil]];
        [self presentViewController:alert animated:YES completion:nil];
        
        
      
        self.currentAlertController = alert;
		return;
        
    }
    
    NSMutableArray *productArray = [[NSMutableArray alloc] init];
    NSData * tempData =[MockModeController getMockResponse:@"ProductXml"];
	ProductXMLHandler *txmlHandler = [[ProductXMLHandler alloc] init];
	txmlHandler.currentData = [ProductXMLHandler encodeDataToData:tempData];
    NSArray *productArrayList=[txmlHandler parseXMLFileAtURL:nil parseError:nil];
    NSString *searchString = [self.itemTextField.text stringByAppendingString:@"**"];
    
    NSLog(@"SearchString:%@",searchString);
    
    NSPredicate *predicate;
    if((self.inventorySegment.selectedSegmentIndex == 1)
       && ![self.manufacturerTextField.text isEqual:@"Any"])
    {
        predicate = [NSPredicate predicateWithFormat:@"(ProductName like[cd] %@ || ProductID like[cd] %@) AND (Manufacturer == %@) AND (QtyOnHand > %@)", searchString, searchString,
                     self.manufacturerTextField.text, @"0"];
    }
    else if((self.inventorySegment.selectedSegmentIndex == 2)
            && ![self.manufacturerTextField.text isEqual:@"Any"])
    {
        predicate = [NSPredicate predicateWithFormat:@"(ProductName like[cd] %@ || ProductID like[cd] %@) AND (Manufacturer == %@) AND (QtyOnHand == %@)", searchString, searchString,
                     self.manufacturerTextField.text, @"0"];
    }
    else if((self.inventorySegment.selectedSegmentIndex == 0) && ![self.manufacturerTextField.text isEqual:@"Any"])
    {
        predicate = [NSPredicate predicateWithFormat:@"(ProductName like[cd] %@ || ProductID like[cd] %@) AND (Manufacturer == %@)", searchString, searchString,
                     self.manufacturerTextField.text];
    }
    else if((self.inventorySegment.selectedSegmentIndex == 1) && [self.manufacturerTextField.text isEqual:@"Any"])
    {
        predicate = [NSPredicate predicateWithFormat:@"(ProductName like[cd] %@ || ProductID like[cd] %@) AND (QtyOnHand > %@)", searchString, searchString, @"0"];
    }
    else if((self.inventorySegment.selectedSegmentIndex == 2) && [self.manufacturerTextField.text isEqual:@"Any"])
    {
        predicate = [NSPredicate predicateWithFormat:@"(ProductName like[cd] %@ || ProductID like[cd] %@) AND (QtyOnHand == %@)", searchString,searchString,@"0"];
    }
    else if((self.inventorySegment.selectedSegmentIndex == 0) && [self.manufacturerTextField.text isEqual:@"Any"])
    {
        predicate = [NSPredicate predicateWithFormat:@"(ProductName like[cd] %@ || ProductID like[cd] %@)", searchString,searchString];
    }
    
	[productArray addObjectsFromArray:productArrayList];
    NSArray *filtered1 = [productArray filteredArrayUsingPredicate:predicate];
    [productArray removeAllObjects];
    [productArray addObjectsFromArray:filtered1];
    
    NSString *androidStr = @"";
    NSString *blackberryStr = @"";
    NSString *iosStr = @"";
    NSString *windowsStr = @"";
    
    if(self.androidSwitch.isOn==YES)
        androidStr=@"Android";
    if(self.blackberrySwitch.isOn==YES)
        blackberryStr=@"BlackBerry";
    if(self.iosSwitch.isOn==YES)
        iosStr=@"iOS";
    if(self.windowsSwitch.isOn==YES)
        windowsStr=@"Windows*";
    
    NSPredicate *predicateInStock=[NSPredicate predicateWithFormat:@"(OperatingSystem like[cd] %@) OR (OperatingSystem like[cd] %@) OR (OperatingSystem like[cd] %@) OR (OperatingSystem like[cd] %@)",
                                   androidStr, blackberryStr, iosStr, windowsStr];
    NSArray *filtered = [productArray filteredArrayUsingPredicate:predicateInStock];
    NSSortDescriptor * sortByFrequency =
    [[NSSortDescriptor alloc] initWithKey:@"ProductName" ascending:YES];
    NSArray * descriptors = [NSArray arrayWithObject:sortByFrequency];
    NSArray * sortedarray = [filtered sortedArrayUsingDescriptors:descriptors];
    [productArray removeAllObjects];
    
    if([filtered count] == 0)
    {
        
        UIAlertController *alert = [UIAlertController alertControllerWithTitle:@"Error!" message:@"Invalid Search Criteria - No search results." preferredStyle:UIAlertControllerStyleAlert];
        [alert addAction:[UIAlertAction actionWithTitle:@"OK" style:UIAlertActionStyleDefault handler:nil]];
        [self presentViewController:alert animated:YES completion:nil];
        
        self.currentAlertController = alert;
        
        return;
    }
    [productArray addObjectsFromArray:sortedarray];
    
    SearchListViewController *searchListController = [[SearchListViewController alloc] initWithNibName:@"SearchListViewController" bundle:nil];
    
    searchListController.title = @"Results";
    searchListController.productArray = productArray;

    [self.navigationController pushViewController:searchListController animated:YES];
    
}

- (NSInteger)numberOfComponentsInPickerView:(UIPickerView *)pickerView
{
	return 1;
}

- (NSInteger)pickerView:(UIPickerView *)pickerView numberOfRowsInComponent:(NSInteger)component
{
	return [Brand count];
}


- (NSString *)pickerView:(UIPickerView *)pickerView titleForRow:(NSInteger)row forComponent:(NSInteger)component{
    return [Brand objectAtIndex:row];
}

- (void)pickerView:(UIPickerView *)pickerView didSelectRow:(NSInteger)row inComponent:(NSInteger)component
{
	self.manufacturerTextField.text = [NSString stringWithFormat:@"%@",[Brand objectAtIndex:row]];
	[self.pickerView reloadAllComponents];
}


@end
