//
//  DetailViewController.m
//  Phone Lookup
//
//  Created by Steve Orlando on 9/7/13.
//  Copyright (c) 2013 Mobile Labs. All rights reserved.
//

#import "DetailViewController.h"
#import "AppDelegate.h"

@implementation DetailViewController
@synthesize controlName;
- (id)initWithNibName:(NSString *)nibNameOrNil bundle:(NSBundle *)nibBundleOrNil
{
    self = [super initWithNibName:nibNameOrNil bundle:nibBundleOrNil];
    if (self) {
        // Custom initialization
    }
    return self;
}

- (void)didReceiveMemoryWarning
{
    // Releases the view if it doesn't have a superview.
    [super didReceiveMemoryWarning];
     
    // Release any cached data, images, etc that aren't in use.
}

#pragma mark - View lifecycle

- (void)viewDidLoad
{

    //self.view.backgroundColor=[UIColor grayColor];
    [super viewDidLoad];
    
    // Do any additional setup after loading the view from its nib.
    /*contolsAppDelegate  *delegate = (contolsAppDelegate *) [[UIApplication sharedApplication] delegate];
    [self.view setBackgroundColor:delegate.bgColor];
    self.title=controlName;
    const char *c = [controlName UTF8String];
    

    if(!NSClassFromString(controlName))
    {
       return;
    }
    id control=objc_getClass(c);
    id classtype=[[[control class] alloc]init];    
    if([[[control class] description] isEqual:@"UIButton"])
        classtype= [[control class] buttonWithType:UIButtonTypeRoundedRect];    
    if([[[control class] description] isEqual:@"UIKeyboardEmojiCategoriesControl"]){
        return;
    }if([[[control class] description] isEqual:@"UISelectionGrabber"])
        return;    
    NSArray *Array= [Util methodNamesForClassNamed:controlName];
    for (NSString *str in Array) {
		//NSLog(@"%@",str);
        if([str isEqual:@"setText:"])
            [classtype setText:controlName];
        if([str isEqual:@"setFrame:"])
            [classtype setFrame:CGRectMake(10, 10, 300, 150)];
        if([str isEqual:@"setTextColor:"])
              [classtype setTextColor:delegate.controlFrColor];
    }  
    [classtype setBackgroundColor:delegate.controlBgColor];
    [self.view addSubview:classtype];*/
    
}

@end
