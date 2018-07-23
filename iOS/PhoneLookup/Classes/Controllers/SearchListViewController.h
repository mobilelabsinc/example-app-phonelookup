//
//  SearchListViewController.h
//  demo
//
//  Created by Steve Orlando on 9/7/13.
//  Copyright (c) 2013 Mobile Labs. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface SearchListViewController : UIViewController <UITableViewDelegate,UITableViewDataSource>
{
    NSArray *productArray;
    IBOutlet UITableView *proTableView;
}

@property (nonatomic,retain) NSArray *productArray;
@property(nonatomic,retain) UITableView *proTableView;

// Used by unit tests; not for production use
+ (SearchListViewController *)currentInstance;

@end
