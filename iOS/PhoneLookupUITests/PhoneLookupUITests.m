//
//  PhoneLookupUITests.m
//  PhoneLookupUITests
//
//  Created by Kris Johnson on 1/30/17.
//  Copyright © 2017 Mobile Labs. All rights reserved.
//

#import <XCTest/XCTest.h>

@interface PhoneLookupUITests : XCTestCase

@property XCUIApplication *app;
@end

@implementation PhoneLookupUITests

- (void)setUp {
    [super setUp];
    
    // In UI tests it is usually best to stop immediately when a failure occurs.
    self.continueAfterFailure = NO;

    // UI tests must launch the application that they test. Doing this in setup will make sure it happens for each test method.
    self.app = [[XCUIApplication alloc] init];
    [self.app launch];
    
    [XCUIDevice sharedDevice].orientation = UIDeviceOrientationPortrait;
    
}

- (void)tearDown {
    [super tearDown];
}

- (void)testUIPickerViewCustom {
    XCUIApplication *app = self.app;
    
    [self enterUsernameAndPasswordAndSignIn];
    
    [app.tabBars.buttons[@"Controls"] tap];

    [app.tables.staticTexts[@"UIPickerViewCustom"] tap];
 
    XCUIElement *picker = app.pickers[@"myPickerView"];
    XCTAssertTrue(picker.exists, @"Picker must exist");
    
    XCUIElement *pickerWheel = [picker.pickerWheels element];
    XCTAssertTrue(pickerWheel.exists, @"Picker wheel must exist");
    
    // This is known to fail.
    // In summary: The picker has custom views, so instead of the value
    // being simply "UILabel", it has the value "UILabel, 1 of 4", and
    // so XCTest fails because it can't find that value in the list
    // of possible values, so it can't determine the current index.
    [pickerWheel adjustToPickerWheelValue:@"UILabel"];
}

// Enter username and password and tap the "Sign In" button on initial screen.
- (void)enterUsernameAndPasswordAndSignIn {
    XCUIApplication *app = self.app;
    
    XCUIElement *usernameTextField = app.textFields[@"UsernameTextField"];
    XCTAssertTrue(usernameTextField.exists, @"Username text field must exist");
    [usernameTextField tap];
    [usernameTextField typeText:@"mobilelabs"];
    
    XCUIElement *passwordTextfield = app.secureTextFields[@"PasswordTextField"];
    XCTAssertTrue(passwordTextfield.exists, @"Password text field must exist");
    [passwordTextfield tap];
    [passwordTextfield typeText:@"demo"];
    
    [app.buttons[@"Done"] tap];
    [app.buttons[@"SignInButton"] tap];
}

@end
