//
//  PhoneLookupTests.m
//  PhoneLookupTests
//
//  Created by Kris Johnson on 7/20/15.
//  Copyright (c) 2015 Mobile Labs. All rights reserved.
//

#import <UIKit/UIKit.h>
#import <XCTest/XCTest.h>

#import "LoginViewController.h"
#import "SearchViewController.h"
#import "SearchListViewController.h"
#import "AppDelegate.h"


// Let the main thread run for the specified time period to allow screen updates to occur
static void updateScreenForTimeInterval(NSTimeInterval timeoutSeconds) {
    NSDate *start = [NSDate date];
    NSDate *timeoutDate = [start dateByAddingTimeInterval:timeoutSeconds];
    do {
        [[NSRunLoop currentRunLoop] runMode:NSDefaultRunLoopMode beforeDate:timeoutDate];
    } while ([[NSDate date] timeIntervalSinceDate:start] < timeoutSeconds);
}


// This delegate class is used in -[PhoneLookupTests testLogin]
@interface TestLoginViewDelegate : NSObject  <LoginViewDelegate>

// This property is set true when -[userDidLoginWithLoginViewController:] is called
@property (nonatomic) BOOL userHasLoggedIn;

// This property is incremented each time -[userLoginDidFailWithLoginViewController:] is called
@property (nonatomic) NSInteger loginFailureCount;

@end

@implementation TestLoginViewDelegate

- (void)userDidLoginWithLoginViewController:(LoginViewController *)view {
    self.userHasLoggedIn = YES;
    
    // Call the app's method to show the next screen
    [[AppDelegate appDelegate] userDidLoginWithLoginViewController:view];
}

- (void)userLoginDidFailWithLoginViewController:(LoginViewController *)view {
    self.loginFailureCount = self.loginFailureCount + 1;
    
    // Show alert and then automatically dismiss it
    UIAlertController *noSessionAlert = [UIAlertController
                                          alertControllerWithTitle:@"Error!"
                                          message:@"Invalid Login Credentials."
                                          preferredStyle:UIAlertControllerStyleAlert];
    [noSessionAlert addAction:[UIAlertAction actionWithTitle:@"OK" style:UIAlertActionStyleDefault handler:nil]];
    
    UIViewController *viewController = [[[[UIApplication sharedApplication] delegate] window] rootViewController];
    if ( viewController.presentedViewController && !viewController.presentedViewController.isBeingDismissed ) {
        viewController = viewController.presentedViewController;
    }
    
    NSLayoutConstraint *constraint = [NSLayoutConstraint
                                      constraintWithItem:noSessionAlert.view
                                      attribute:NSLayoutAttributeHeight
                                      relatedBy:NSLayoutRelationLessThanOrEqual
                                      toItem:nil
                                      attribute:NSLayoutAttributeNotAnAttribute
                                      multiplier:1
                                      constant:viewController.view.frame.size.height*2.0f];
    
    [noSessionAlert.view addConstraint:constraint];
    [viewController presentViewController:noSessionAlert animated:YES completion:^{}];
    
    updateScreenForTimeInterval(1.0);
    [noSessionAlert dismissViewControllerAnimated:YES completion:nil];
    updateScreenForTimeInterval(1.0);
}

@end


@interface PhoneLookupTests : XCTestCase

@end

@implementation PhoneLookupTests

- (void)setUp {
    [super setUp];
    // Put setup code here. This method is called before the invocation of each test method in the class.
}

- (void)tearDown {
    // Put teardown code here. This method is called after the invocation of each test method in the class.
    [super tearDown];
}

- (void)testLogin {
    LoginViewController *loginViewController = [AppDelegate loginViewController];
    
    // Construct our test delegate and assign it to the view controller
    id<LoginViewDelegate> originalDelegate = loginViewController.delegate;
    TestLoginViewDelegate *delegate = [TestLoginViewDelegate new];
    loginViewController.delegate = delegate;
    
    XCTAssertFalse(delegate.userHasLoggedIn, @"userHasLoggedIn should be false when the delegate is initialized");
    XCTAssertEqual(0, delegate.loginFailureCount, @"loginFailureCount should be zero when the delegate is initialized");
    
    
    // Turn off the "Remember Me" switch so that none of our test values get saved
    [loginViewController setSwitchControlOn:NO];
    
    
    // Simulate a login with empty username and password
    
    [loginViewController setUsernameText:@""];
    [loginViewController setPasswordText:@""];
    updateScreenForTimeInterval(0.5);
    [loginViewController loginAction:nil];
    
    XCTAssertFalse(delegate.userHasLoggedIn, @"userHasLoggedIn should still be false after attempt to log in with no username or password");
    XCTAssertEqual(1, delegate.loginFailureCount, @"one login failure should be logged");
    
    
    // Simulate login with invalid username and password
    
    [loginViewController setUsernameText:@"xyzzy"];
    updateScreenForTimeInterval(0.5);
    
    [loginViewController setPasswordText:@"foobar"];
    updateScreenForTimeInterval(2.0);
    
    [loginViewController loginAction:nil];
    updateScreenForTimeInterval(0.5);
    
    XCTAssertFalse(delegate.userHasLoggedIn, @"userHasLoggedIn should still be false after attempt to log in with invalid username and password");
    XCTAssertEqual(2, delegate.loginFailureCount, @"a second login failure should be logged");
    
    
    // Simulate login with valid username and password
    
    [loginViewController setUsernameText:@"mobilelabs"];
    updateScreenForTimeInterval(0.5);
    
    [loginViewController setPasswordText:@"demo"];
    updateScreenForTimeInterval(2.0);
    
    [loginViewController loginAction:nil];
    updateScreenForTimeInterval(2.0);
    
    XCTAssertTrue(delegate.userHasLoggedIn, @"userHasLoggedIn should still be true after valid username and password");
    XCTAssertEqual(2, delegate.loginFailureCount, @"login failure count should be unchanged with valid username and password");
    
    
    // Restore original state of login controller
    loginViewController.delegate = originalDelegate;
    
    SearchViewController *searchController = [SearchViewController currentInstance];
    XCTAssertNotNil(searchController, @"should have shown a SearchViewController");
    
    // Deselect all switches and try a search (which should fail)
    searchController.androidSwitch.on = NO;
    updateScreenForTimeInterval(0.25);
    searchController.iosSwitch.on = NO;
    updateScreenForTimeInterval(0.25);
    searchController.blackberrySwitch.on = NO;
    updateScreenForTimeInterval(0.25);
    searchController.windowsSwitch.on = NO;
    updateScreenForTimeInterval(0.25);
    [searchController search:nil];
    updateScreenForTimeInterval(2.0);
    
    XCTAssertNotNil(searchController.currentAlertController, @"should have displayed an alert because there are no matches");
    [searchController.currentAlertController dismissViewControllerAnimated:YES completion:nil];
    updateScreenForTimeInterval(2.0);
    
    // Select all switches and try a search (which should succeed)
    searchController.androidSwitch.on = YES;
    updateScreenForTimeInterval(0.5);
    searchController.iosSwitch.on = YES;
    updateScreenForTimeInterval(0.5);
    searchController.blackberrySwitch.on = YES;
    updateScreenForTimeInterval(0.5);
    searchController.windowsSwitch.on = YES;
    updateScreenForTimeInterval(0.5);
    
    [searchController search:nil];
    updateScreenForTimeInterval(3.0);
    
    SearchListViewController *listController = [SearchListViewController currentInstance];
    XCTAssertNotNil(listController, @"should have gone to SearchListController");
    
    // Go back to the search screen
    [[listController navigationController] popViewControllerAnimated:YES];
    updateScreenForTimeInterval(4.0);
}

@end
