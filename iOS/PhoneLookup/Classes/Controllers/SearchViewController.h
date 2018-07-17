//
//  SearchViewController.h
//  demo
//
//  Created by Steve Orlando on 9/7/13.
//  Copyright (c) 2013 Mobile Labs. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface SearchViewController : UIViewController <UITextFieldDelegate>{
    UINavigationController *navigation;
    NSMutableArray *Brand;
    UILabel *itemLabel;
    UILabel *manufacturerLabel;
    UILabel *osLabel;
    UILabel *androidLabel;
    UILabel *iosLabel;
    UILabel *blackberryLabel;
    UILabel *windowsLabel;
    UILabel *inventoryLabel;
}


@property (retain, nonatomic) IBOutlet UITextField *itemTextField;
@property (retain, nonatomic) IBOutlet UITextField *manufacturerTextField;
@property (retain, nonatomic) IBOutlet UISwitch *androidSwitch;
@property (retain, nonatomic) IBOutlet UISwitch *blackberrySwitch;
@property (retain, nonatomic) IBOutlet UISwitch *iosSwitch;
@property (retain, nonatomic) IBOutlet UISwitch *windowsSwitch;
@property (retain, nonatomic) IBOutlet UISegmentedControl *inventorySegment;
@property (retain, nonatomic) IBOutlet UIToolbar *pickerToolbar;
@property (retain, nonatomic) IBOutlet UIPickerView *pickerView;
@property (retain, nonatomic) IBOutlet UIBarButtonItem *doneBarButtonItem;
@property (retain, nonatomic) IBOutlet UIButton *searchButton;
@property (nonatomic,retain) UINavigationController *navigation;


-(IBAction) doneEditing:(id)sender;
-(IBAction) search:(id)sender;

// Used by unit tests; not for production use
+ (SearchViewController *)currentInstance;
@property (nonatomic) UIAlertController *currentAlertController;

@end
