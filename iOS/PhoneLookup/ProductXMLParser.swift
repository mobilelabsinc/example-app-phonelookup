//
//  ProductXMLParser.swift
//  PhoneLookup
//
//  Created by Steve Orlando on 7/22/18.
//  Copyright © 2018 Mobile Labs. All rights reserved.
//

import Foundation

class ProductXMLParser : NSObject, XMLParserDelegate {
    var XMLfile : InputStream
    var products = [Item]()
    var currentValue = ""
    var item = Item()
    var manufacturerList = [String]()
    var elementNames = ["ProductID", "ProductName", "Manufacturer", "Description", "Price", "InStock", "OperatingSystem", "QtyOnHand", "Carrier"]
    var parser : XMLParser
    var currentItem : Item?
    var parsedItems : [Item]?
    
    subscript(i: Int) -> Item {
        if let items = getParsedItems() {
            return items[i]
        } else {
            return Item()
        }
    }
    
    init(forResource XmlFile : String, withExtension ext : String){
        let path = Bundle.main.path(forResource: "ProductXml", ofType: "xml")
        XMLfile = InputStream(fileAtPath: path!)!
        parser = XMLParser(stream: XMLfile)
    }
    
    func getParsedItems() -> [Item]? {
        start()
        return parsedItems
    }
    
    func getManufacuterers() -> [String]? {
        start()
        return manufacturerList
    }
    
    func start() {
        currentItem = nil
        parsedItems = []
        currentValue = ""
        parser.delegate = self
        parser.parse()
    }
    
    
    func parser(_ parser : XMLParser, didStartElement elementName: String, namespaceURI: String?, qualifiedName: String?, attributes: [String : String] = [:]) {
        if elementName == "Items" {
            currentItem = Item()
        } else if elementNames.contains(elementName) {
            
        }
        currentValue = ""
    }
    
    func parser(_ parser: XMLParser, didEndElement elementName: String, namespaceURI: String?, qualifiedName qName: String?) {
        if let item = currentItem {
            switch elementName {
            case "ProductID" :
                item.productID = currentValue
            case "ProductName" :
                item.productName = currentValue
            case "Manufacturer" :
                item.manufacturer = currentValue
            case "Description" :
                item.productDescription = currentValue
            case "Price" :
                item.price = currentValue
            case "InStock" :
                item.inStock = currentValue
            case "OperatingSystem" :
                item.operatingSystem = currentValue
            case "QtyOnHand" :
                item.qtyOnHand = currentValue
            case "Carrier" :
                item.carrier = currentValue
            case "Items" :
                parsedItems!.append(currentItem!)
                if (!manufacturerList.contains((currentItem?.manufacturer)!)){
                    manufacturerList.append((currentItem?.manufacturer)!)
                }
            default:
                break
            }
            
        }
    }
    
    func parser(_ parser: XMLParser, foundCharacters string: String) {
        currentValue += string
    }
    
}
