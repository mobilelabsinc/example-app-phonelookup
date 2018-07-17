//
//  AppDelegate.h
//  demo
//
//  Created by Steve Orlando on 9/7/13.
//  Copyright (c) 2013 Mobile Labs. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "LoginViewController.h"

@interface AppDelegate : UIResponder <UIApplicationDelegate, UITabBarControllerDelegate, LoginViewDelegate>
{

}
@property (strong, nonatomic) UIWindow *window;
@property (strong, nonatomic) UITabBarController *tabBarController;


// Get reference to the AppDelegate instance.
//
// This is intended for automated testing only.
+ (AppDelegate *)appDelegate;

// Get the login controller shown at application launch.
//
// This is intended for automated testing only.
+ (LoginViewController *)loginViewController;

@end
