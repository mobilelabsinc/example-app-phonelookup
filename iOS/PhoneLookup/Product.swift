//
//  ProductClass.swift
//  PhoneLookup
//
//  Created by Steve Orlando on 7/22/18.
//  Copyright © 2018 Mobile Labs. All rights reserved.
//

import Foundation

@objc class Product: NSObject {
    var items : [Item] = []
    
    @objc func getManufacturers() -> Array<String> {
        var manufacturers = [String]()
        
        let productParser = ProductXMLParser(forResource: "ProductXml", withExtension: "xml")
        manufacturers = productParser.getManufacuterers()!
        manufacturers.append("Any")
        let manufacturersSorted = manufacturers.sorted { $0.localizedCaseInsensitiveCompare($1) == ComparisonResult.orderedAscending }
        
        return manufacturersSorted
    }
    
    @objc func getProducts(searchValue : String, manufacturer : String, ios : Bool, android : Bool, blackberry : Bool, windows : Bool, inventory : NSInteger) -> Array<Item> {
        var allProducts = [Item]()
        var products = [Item]()
        
        let productParser = ProductXMLParser(forResource: "ProductXml", withExtension: "xml")
        allProducts = productParser.getParsedItems()!
        
        for item in allProducts {
        
            if (manufacturer == "Any" || manufacturer == item.manufacturer) {
                if ((inventory == 0) || (inventory == 1 && item.inStock == "Y") || (inventory == 2 && item.inStock == "N")) {
                    if ((android && item.operatingSystem.contains("Android")) || (ios && item.operatingSystem.contains("iOS")) || (blackberry && item.operatingSystem.contains("BlackBerry")) || (windows && item.operatingSystem.contains("Windows"))) {
                        if ((item.productName.lowercased().starts(with: searchValue.lowercased())) || (item.productID.starts(with: searchValue))) {
                            
                            products.append(item);
                        }
                    }
                    
                }
            }
        }
        
        
        
        return products
    }
}
