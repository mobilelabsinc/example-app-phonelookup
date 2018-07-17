//
//  AppDelegate.m
//  demo
//
//  Created by Steve Orlando on 9/7/13.
//  Copyright (c) 2013 Mobile Labs. All rights reserved.
//

#import "AppDelegate.h"
#import "SearchViewController.h"
#import "LoginViewController.h"

static AppDelegate *appDelegateInstance;
static LoginViewController *loginViewControllerInstance;


@implementation AppDelegate

@synthesize window = _window;
@synthesize tabBarController = _tabBarController;

+ (AppDelegate *)appDelegate {
    return appDelegateInstance;
}

+ (LoginViewController *)loginViewController {
    return loginViewControllerInstance;
}

- (BOOL)application:(UIApplication *)application didFinishLaunchingWithOptions:(NSDictionary *)launchOptions
{
    appDelegateInstance = self;
    
    self.window = [[UIWindow alloc] initWithFrame:[[UIScreen mainScreen] bounds]];

    // Override point for customization after application launch.
    self.tabBarController = [[UITabBarController alloc] initWithNibName:nil bundle:nil];
    
    SearchViewController *searchViewController = [[SearchViewController alloc] initWithNibName:@"SearchViewController" bundle:nil];
    UINavigationController *searchNavController = [[UINavigationController alloc] initWithRootViewController:searchViewController];
    [searchNavController.tabBarItem initWithTabBarSystemItem:UITabBarSystemItemSearch tag:0];
    searchNavController.tabBarItem.title = @"Search";
    
    
    self.tabBarController.viewControllers = [NSArray arrayWithObjects: searchNavController, nil];
    

    //Show the login screen as a modal
    LoginViewController *loginViewController = [[LoginViewController alloc]
                                                initWithNibName:@"LoginViewController" bundle:nil];
    loginViewControllerInstance = loginViewController;
    loginViewController.delegate = self;
    self.window.rootViewController = loginViewController;
    [self.window makeKeyAndVisible];
    
    return YES;
}

- (void)applicationWillResignActive:(UIApplication *)application
{
    // Sent when the application is about to move from active to inactive state. This can occur for certain types of temporary interruptions (such as an incoming phone call or SMS message) or when the user quits the application and it begins the transition to the background state.
    // Use this method to pause ongoing tasks, disable timers, and throttle down OpenGL ES frame rates. Games should use this method to pause the game.
}

- (void)applicationDidEnterBackground:(UIApplication *)application
{
    // Use this method to release shared resources, save user data, invalidate timers, and store enough application state information to restore your application to its current state in case it is terminated later. 
    // If your application supports background execution, this method is called instead of applicationWillTerminate: when the user quits.
}

- (void)applicationWillEnterForeground:(UIApplication *)application
{
    // Called as part of the transition from the background to the inactive state; here you can undo many of the changes made on entering the background.
}

- (void)applicationDidBecomeActive:(UIApplication *)application
{
    // Restart any tasks that were paused (or not yet started) while the application was inactive. If the application was previously in the background, optionally refresh the user interface.
}

- (void)applicationWillTerminate:(UIApplication *)application
{
    // Called when the application is about to terminate. Save data if appropriate. See also applicationDidEnterBackground:.
}

- (void)userDidLoginWithLoginViewController:(LoginViewController *)view;
{
    [self.window.rootViewController presentViewController:self.tabBarController animated:YES completion:NULL];
}

@end
