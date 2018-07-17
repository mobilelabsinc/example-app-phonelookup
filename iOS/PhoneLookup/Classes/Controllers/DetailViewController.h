//
//  DetailViewController.h
//  Phone Lookup
//
//  Created by Steve Orlando on 9/7/13.
//  Copyright (c) 2013 Mobile Labs. All rights reserved.
//

#import <UIKit/UIKit.h>
#import <objc/runtime.h>

@interface DetailViewController : UIViewController {
    NSString *controlName;
}
@property (nonatomic,retain)  NSString *controlName;
@end
