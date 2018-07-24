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
#import "PhoneLookup-Swift.h"

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
    
    //Build Manufacturers Picker Data
    Product *product = [[Product alloc] init];
    manufacturers = [product getManufacturers];
    
	// Do any additional setup after loading the view from its nib.
    [self.itemTextField setText:@""];
    [self.manufacturerTextField setText:[NSString stringWithFormat:@"%@",[manufacturers objectAtIndex:1]]];
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
    //Alert if no operating is set. Required to have operating system for search
    if((self.androidSwitch.isOn == NO) && (self.blackberrySwitch.isOn == NO)
       && (self.iosSwitch.isOn == NO) && (self.windowsSwitch.isOn == NO))
    {
        UIAlertController *alert = [UIAlertController alertControllerWithTitle:@"Error!" message:@"Invalid Selection Criteria" preferredStyle:UIAlertControllerStyleAlert];
        [alert addAction:[UIAlertAction actionWithTitle:@"OK" style:UIAlertActionStyleDefault handler:nil]];
        [self presentViewController:alert animated:YES completion:nil];
        
        self.currentAlertController = alert;
		return;
    }
    
    //Return an array of matching product items
    Product *product = [[Product alloc] init];
    NSArray *productArray = [product getProductsWithSearchValue:self.itemTextField.text manufacturer:self.manufacturerTextField.text ios:self.iosSwitch.isOn android:self.androidSwitch.isOn blackberry:self.blackberrySwitch.isOn windows:self.windowsSwitch.isOn inventory:self.inventorySegment.selectedSegmentIndex];
    
    if([productArray count] == 0)
    {
        
        UIAlertController *alert = [UIAlertController alertControllerWithTitle:@"Error!" message:@"Invalid Search Criteria - No search results." preferredStyle:UIAlertControllerStyleAlert];
        [alert addAction:[UIAlertAction actionWithTitle:@"OK" style:UIAlertActionStyleDefault handler:nil]];
        [self presentViewController:alert animated:YES completion:nil];
        
        self.currentAlertController = alert;
        
        return;
    }
    
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
	return [manufacturers count];
}


- (NSString *)pickerView:(UIPickerView *)pickerView titleForRow:(NSInteger)row forComponent:(NSInteger)component{
        return manufacturers[row];
}

- (void)pickerView:(UIPickerView *)pickerView didSelectRow:(NSInteger)row inComponent:(NSInteger)component
{
	self.manufacturerTextField.text = manufacturers[row];
}


@end
