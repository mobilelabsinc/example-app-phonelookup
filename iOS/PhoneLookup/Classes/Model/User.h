//
//  User.h
//  Phone Lookup
//
//  Created by Steve Orlando on 9/7/13.
//  Copyright (c) 2013 Mobile Labs. All rights reserved.
//

#import <Foundation/Foundation.h>


@interface User : NSObject {
    NSString *username;
	NSString *password;
}
@property (nonatomic, retain) NSString *username;
@property (nonatomic, retain) NSString *password;
@end
