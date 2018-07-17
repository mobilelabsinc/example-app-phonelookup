//
//  LoginViewController.m
//  demo
//
//  Created by Steve Orlando on 9/7/13.
//  Copyright (c) 2013 Mobile Labs. All rights reserved.
//

#import "LoginViewController.h"
#import "MockModeController.h"
#import "ProductXMLHandler.h"

@interface LoginViewController ()

@end

@implementation LoginViewController

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
    [super viewDidLoad];
    
    luserName.accessibilityIdentifier = @"UsernameLabel";
    username.accessibilityIdentifier = @"UsernameTextField";
    lPassword.accessibilityIdentifier = @"PasswordLabel";
    password.accessibilityIdentifier = @"PasswordTextField";
    lRememberMe.accessibilityIdentifier = @"RememberMeLabel";
    switchCntl.accessibilityIdentifier = @"RememberMeSwitch";
    bSignIn.accessibilityIdentifier = @"SignInButton";
    
    
    username.clearButtonMode = UITextFieldViewModeWhileEditing;
    password.clearButtonMode = UITextFieldViewModeWhileEditing;
    
    NSUserDefaults *userDefaults = [NSUserDefaults standardUserDefaults];
    NSString *name = (NSString *)[userDefaults objectForKey:@"USER"];
    NSString *pass = (NSString *)[userDefaults objectForKey:@"PASS"];
    NSString *rme = (NSString *)[userDefaults objectForKey:@"RME"];
    if(rme==nil || [rme isEqualToString:@"NO"])
    {
        switchCntl.on=NO;
    }
    else {
        switchCntl.on=YES;
        username.text=name;
        username.textColor=[UIColor blackColor];
        password.text=pass;
        password.textColor=[UIColor blackColor];
    }
}

- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

- (IBAction) loginAction:(UIButton *)sender
{
    NSData * tempData =[MockModeController getMockResponse:@"LoginXml"];
    ProductXMLHandler *txmlHandler = [[ProductXMLHandler alloc] init];
    txmlHandler.currentData = [ProductXMLHandler encodeDataToData:tempData];
    NSArray *userArray=[txmlHandler parseXMLFileAtURL:nil parseError:nil];
    
    NSArray *filtered= [userArray filteredArrayUsingPredicate:[NSPredicate predicateWithFormat:@"(username == %@) and (password == %@)",[username.text uppercaseString],[password.text uppercaseString]]];
    
    if([filtered count]==0){
        if ([self.delegate respondsToSelector:@selector(userLoginDidFailWithLoginViewController:)]) {
            [self.delegate userLoginDidFailWithLoginViewController:self];
        }
        else {
            UIAlertController *noSessionAlert = [UIAlertController
                                                 alertControllerWithTitle:@"Error!"
                                                 message:@"Invalid Login Credentials."
                                                 preferredStyle:UIAlertControllerStyleAlert];
            [noSessionAlert addAction:[UIAlertAction actionWithTitle:@"OK" style:UIAlertActionStyleDefault handler:nil]];
            [self presentViewController:noSessionAlert animated:YES completion:nil];
            
        }
        return;
    }
    
    NSUserDefaults *userDefaults = [NSUserDefaults standardUserDefaults];
    
    if(switchCntl.on)
    {
        [userDefaults setObject:username.text forKey:@"USER"];
        [userDefaults setObject:password.text forKey:@"PASS"];
        [userDefaults setObject:@"YES" forKey:@"RME"];
        
    }
    else {
        [userDefaults setObject:@"" forKey:@"USER"];
        [userDefaults setObject:@"" forKey:@"PASS"];
        [userDefaults setObject:@"NO" forKey:@"RME"];
        username.text=@"";
        password.text=@"";
    }
    
    if ([self.delegate respondsToSelector:@selector(userDidLoginWithLoginViewController:)]) {
        [self.delegate userDidLoginWithLoginViewController:self];
    }
}

#pragma mark -
#pragma mark Text Field Delegate
- (void)textFieldDidBeginEditing:(UITextField *)textField
{
    
}

- (void)textFieldDidEndEditing:(UITextField *)textField
{
    [textField resignFirstResponder];
}

- (BOOL)textFieldShouldReturn:(UITextField *)textField
{
    [textField resignFirstResponder];
    return YES;
}


#pragma mark - Unit test support

- (void)setUsernameText:(NSString *)text {
    username.text = text;
}

- (void)setPasswordText:(NSString *)text {
    password.text = text;
}

- (void)setSwitchControlOn:(BOOL)on {
    switchCntl.on = on;
}

@end
