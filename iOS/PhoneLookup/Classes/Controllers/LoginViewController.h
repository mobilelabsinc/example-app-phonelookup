//
//  LoginViewController.h
//  demo
//
//  Created by Steve Orlando on 9/7/13.
//  Copyright (c) 2013 Mobile Labs. All rights reserved.
//

#import <UIKit/UIKit.h>

@class LoginViewController;

@protocol LoginViewDelegate <NSObject>

@optional
- (void)userDidLoginWithLoginViewController:(LoginViewController *)view;
- (void)userLoginDidFailWithLoginViewController:(LoginViewController *)view;
@end

@interface LoginViewController : UIViewController <UITextFieldDelegate> 
{
    IBOutlet UITextField *username;
    IBOutlet UITextField *password;
    IBOutlet UILabel *luserName;
    IBOutlet UILabel *lPassword;
	IBOutlet UILabel *lRememberMe;
	IBOutlet UISwitch *switchCntl;
    IBOutlet UIButton *bSignIn;
}

@property (weak) id<LoginViewDelegate> delegate;
- (IBAction) loginAction:(UIButton *)sender;

// These methods are used by unit tests
- (void)setUsernameText:(NSString *)text;
- (void)setPasswordText:(NSString *)text;
- (void)setSwitchControlOn:(BOOL)on;

@end
