//
//  ProductXMLHandler.m
//  Phone Lookup
//
//  Powered by Smartsoft Mobile Solutions on 09/09/11.
//  Copyright 2011 Smartsoft Mobile Solutions. All rights reserved.
//

#import "ProductXMLHandler.h"


@implementation ProductXMLHandler
@synthesize objectArray, contentOfCurrentProperty, currentData;


- (NSMutableArray *)parseXMLFileAtURL:(NSURL *)URL parseError:(NSError **)error
{
	objectArray = [[NSMutableArray alloc] init];
	//NSXMLParser *parser = [[NSXMLParser alloc] initWithContentsOfURL:URL];
    NSXMLParser *parser = [[NSXMLParser alloc] initWithData:currentData];
	// Set self as the delegate of the parser so that it will receive the parser delegate methods callbacks.
    [parser setDelegate:self];
    // Depending on the XML document you're parsing, you may want to enable these features of NSXMLParser.
    [parser setShouldProcessNamespaces:NO];
    [parser setShouldReportNamespacePrefixes:NO];
    [parser setShouldResolveExternalEntities:NO];    
    [parser parse];
	
	return objectArray;
}
- (void)parser:(NSXMLParser *)_parser didStartElement:(NSString *)elementName namespaceURI:(NSString *)namespaceURI qualifiedName:(NSString *)qName attributes:(NSDictionary *)attributeDict
{
	
	
    if (qName) 
        elementName = qName;
    
	attDict = attributeDict;
	//NSLog(@"elementName: %@",elementName);
	self.contentOfCurrentProperty = [NSMutableString string];
}


- (void)parser:(NSXMLParser *)_parser didEndElement:(NSString *)elementName namespaceURI:(NSString *)namespaceURI qualifiedName:(NSString *)qName
{     
    if (qName) 
        elementName = qName;
	
	//NSLog(@"elementName: %@ - %@",elementName,contentOfCurrentProperty);
	if ([elementName isEqualToString:@"ProductID"]) 
	{
        //[oneRec release];
		oneRec=[[ProductItem alloc] init];
		oneRec.ProductID=contentOfCurrentProperty;
	}	
	else if ([elementName isEqualToString:@"ProductName"]) 
	{
		oneRec.ProductName=contentOfCurrentProperty;
	}
	else if ([elementName isEqualToString:@"Description"]) 
	{
		oneRec.Description=contentOfCurrentProperty;
	}
	
	else if ([elementName isEqualToString:@"Price"]) 
	{
		oneRec.Price=contentOfCurrentProperty;
	}
	
	else if ([elementName isEqualToString:@"InStock"]) 
	{
		oneRec.InStock=contentOfCurrentProperty;
	}else if ([elementName isEqualToString:@"Manufacturer"]) 
	{
		oneRec.Manufacturer=contentOfCurrentProperty;		
	}
	
	else if ([elementName isEqualToString:@"QtyOnHand"]) 
	{
		oneRec.QtyOnHand=contentOfCurrentProperty;
	}else if ([elementName isEqualToString:@"OperatingSystem"]) 
	{
		oneRec.OperatingSystem=contentOfCurrentProperty;
	}else if ([elementName isEqualToString:@"Carrier"]) 
	{
		oneRec.Carrier=contentOfCurrentProperty;
		[objectArray addObject:oneRec];
	}    
    else if ([elementName isEqualToString:@"username"]) 
	{
        user=[[User alloc] init];
		user.username=[contentOfCurrentProperty uppercaseString];
	}
    else if ([elementName isEqualToString:@"password"]) 
	{
		user.password=[contentOfCurrentProperty uppercaseString];
        [objectArray addObject:user];
	}else if ([elementName isEqualToString:@"Item"]) 
	{
        [objectArray addObject:contentOfCurrentProperty];
	}
}



- (void)parser:(NSXMLParser *)_parser foundCharacters:(NSString *)string
{
	
    if (self.contentOfCurrentProperty && string != nil)
        [self.contentOfCurrentProperty appendString:[string stringByTrimmingCharactersInSet:[NSCharacterSet whitespaceAndNewlineCharacterSet]]];
}

- (void)parser:(NSXMLParser *)_parser foundCDATA:(NSData *)CDATABlock;
{
	self.contentOfCurrentProperty = [[NSMutableString alloc] initWithData:CDATABlock encoding:NSUTF8StringEncoding];
}
+ (NSData *)encodeDataToData:(NSData *)s
{
	
	NSString *testing =[[NSString alloc] initWithData:s encoding:NSUTF8StringEncoding];
	testing =[testing stringByReplacingOccurrencesOfString:@"&lt;" withString:@"<"];
	testing =[testing stringByReplacingOccurrencesOfString:@"&gt;" withString:@">"];
	testing =[testing stringByReplacingOccurrencesOfString:@"ï»¿" withString:@""];
	testing =[testing stringByReplacingOccurrencesOfString:@"<?xml version=\"1.0\" encoding=\"utf-8\"?>" withString:@""];
	testing =[testing stringByReplacingOccurrencesOfString:@"&amp;apos;" withString:@" "];
	
	//NSLog(@"XML Response  : %@ ",testing);
	return [testing dataUsingEncoding:NSUTF8StringEncoding];
}


@end