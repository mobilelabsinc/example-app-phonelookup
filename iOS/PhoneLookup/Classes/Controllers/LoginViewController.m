//
//  LoginViewController.m
//  demo
//
//  Created by Steve Orlando on 9/7/13.
//  Copyright (c) 2013 Mobile Labs. All rights reserved.
//

#import "LoginViewController.h"

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
    bSignIn.accessibilityIdentifier = @"SignInButton";
    
    username.clearButtonMode = UITextFieldViewModeWhileEditing;
    password.clearButtonMode = UITextFieldViewModeWhileEditing;
}

- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

- (IBAction) loginAction:(UIButton *)sender
{
    if(! ([username.text.uppercaseString isEqualToString:@"MOBILELABS"] && [password.text.uppercaseString isEqualToString:@"DEMO"])){
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
