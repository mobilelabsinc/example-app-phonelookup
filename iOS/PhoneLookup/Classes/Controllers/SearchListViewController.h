//
//  SearchListViewController.h
//  demo
//
//  Created by Steve Orlando on 9/7/13.
//  Copyright (c) 2013 Mobile Labs. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "ProductItem.h"


@interface SearchListViewController : UIViewController <UITableViewDelegate,UITableViewDataSource>
{
    NSMutableArray *productArray;
    IBOutlet UITableView *proTableView;
    ProductItem *productItem;
    
}

@property (nonatomic,retain) NSMutableArray *productArray;
@property(nonatomic,retain) UITableView *proTableView;
@property(nonatomic,retain) ProductItem *productItem;

// Used by unit tests; not for production use
+ (SearchListViewController *)currentInstance;

@end
