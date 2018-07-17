//
//  ProductXMLHandler.h
//  Phone Lookup
//
//  Powered by Smartsoft Mobile Solutions on 09/09/11.
//  Copyright 2011 Smartsoft Mobile Solutions. All rights reserved.
//

#import <Foundation/Foundation.h>
#import "ProductItem.h"
#import "User.h"

@interface ProductXMLHandler : NSObject<NSXMLParserDelegate> {
	NSMutableArray *objectArray;
	NSDictionary *attDict;
	NSMutableString * contentOfCurrentProperty;
	NSData *currentData;
	ProductItem * oneRec;
    User *user;
}
@property (nonatomic, retain) NSData *currentData;
@property (nonatomic, retain) NSMutableArray *objectArray;
@property (nonatomic, retain) NSMutableString * contentOfCurrentProperty;
- (NSMutableArray *)parseXMLFileAtURL:(NSURL *)URL parseError:(NSError **)error;
+ (NSData *)encodeDataToData:(NSData *)s;
@end
