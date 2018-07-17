//
//  MockModeController.m
//  Phone Lookup
//
//  Powered by Smartsoft Mobile Solutions on 09/09/11.
//  Copyright 2011 Smartsoft Mobile Solutions. All rights reserved.
//

#import "MockModeController.h"


@implementation MockModeController

+ (id) getMockResponse:(NSString *)messageType
{
	NSData *resData = nil;
	NSString *fileName;
	NSString *xmlStr;
	if([messageType isEqual:@"ProductXml"])
		fileName = @"ProductXml";
    else if([messageType isEqual:@"LoginXml"])
		fileName = @"LoginXml";
    NSString *filePath = [[NSBundle mainBundle] pathForResource:fileName ofType:@"xml"];
	if (filePath) {  
		xmlStr = [NSString stringWithContentsOfFile:filePath encoding:NSUTF8StringEncoding error:nil];  
	}
	resData = [xmlStr dataUsingEncoding:NSUTF8StringEncoding];		
	return resData;
}
@end
