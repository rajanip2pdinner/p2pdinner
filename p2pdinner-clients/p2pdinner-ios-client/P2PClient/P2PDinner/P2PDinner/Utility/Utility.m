//
//  Utility.m
//  P2PDinner
//
//  Created by sudheerkumar on 2/6/15.
//  Copyright (c) 2015 P2PDinner. All rights reserved.
//

#import "Utility.h"
#import <objc/runtime.h>
#import "AppConstants.h"
#import "AFImageRequestOperation.h"
#import "AppDelegate.h"

// helper function: get the string form of any object
static NSString *toString(id object) {
    return [NSString stringWithFormat: @"%@", object];
}

// helper function: get the url encoded string form of any object
static NSString *urlEncode(id object) {
    NSString *string = toString(object);
    return [string stringByAddingPercentEscapesUsingEncoding: NSUTF8StringEncoding];
}

@implementation Utility

+ (NSString *)dateToStringFormat:(NSDate *)dateValue timeZone:(TimeZone)timeZone{
    return [self dateToStringFormat:@"MMM dd YYYY" dateString:dateValue timeZone:timeZone];
}

+ (NSString *)dateToStringFormat:(NSString *)format dateString:(NSDate *)dateValue timeZone:(TimeZone)timeZone{
    NSTimeZone *inputTimeZone;
    
    if (timeZone==LOCAL) {
        inputTimeZone = [NSTimeZone localTimeZone];
    }
    else
    {
        inputTimeZone= [NSTimeZone timeZoneWithAbbreviation:@"UTC"];
    }
    
    NSDateFormatter *formatter = [[NSDateFormatter alloc] init];
    [formatter setTimeZone:inputTimeZone];
    [formatter setDateFormat:format];
    return [formatter stringFromDate:dateValue];
}

+   (NSString *)jsonStringToURLParameter:(NSString *)jsonString{
       NSData *objectData = [jsonString dataUsingEncoding:NSUTF8StringEncoding];
      NSError *jsonError=nil;
 NSDictionary *jsonDictonary = [NSJSONSerialization JSONObjectWithData:objectData
                                                                  options:NSJSONReadingMutableContainers
                                                                    error:&jsonError];
    
    return [self dictionaryStringToURLParameter:jsonDictonary];
}

+ (NSString *)dictionaryStringToURLParameter:(NSDictionary *)jsonDictionary{
     NSMutableArray *parts = [NSMutableArray array];
    for (id key in jsonDictionary) {
        id value = [jsonDictionary objectForKey: key];
        NSString *part = [NSString stringWithFormat: @"%@=%@", urlEncode(key), urlEncode(value)];
        [parts addObject: part];
    }
    return [parts componentsJoinedByString: @"&"];
}

+ (NSDictionary *)jsonString:(NSString *)jsonStr{
         NSData *objectData = [jsonStr dataUsingEncoding:NSUTF8StringEncoding];
        NSError *jsonError=nil;
    return [NSJSONSerialization JSONObjectWithData:objectData options:NSJSONReadingMutableContainers error:&jsonError];
}
/**
 *  method used to get the size of string
 *
 *  @param string   NSString
 *  @param fontType paramter to define the font family
 *
 *  @return CGSize
 */
+ (CGSize)getSizeOfString:(NSString *)string OfFont:(UIFont *)fontType{
    //check nil string
    
    if (!string.length>0) {
        string=@"";
    }
    
    if (!fontType) fontType = [UIFont systemFontOfSize:[UIFont labelFontSize]]; //SPHONE-10940
    
    CGSize size = [string sizeWithAttributes:
                   @{NSFontAttributeName:fontType}];
    
    // Values are fractional -- you should take the ceilf to get equivalent values
    size.height = ceilf((float)size.height);
     size.width = ceilf((float)size.width);
    return size;
}

+ (NSDate *)stringToDateFormat:(NSString *)format dateString:(NSString *)dateValue timeZone:(TimeZone)timeZone{
    NSTimeZone *inputTimeZone;
    
    if (timeZone==LOCAL) {
        inputTimeZone = [NSTimeZone localTimeZone];
    }
    else
    {
        inputTimeZone= [NSTimeZone timeZoneWithAbbreviation:@"UTC"];
    }
    
    NSDateFormatter *formatter = [[NSDateFormatter alloc] init];
    [formatter setDateFormat:format];
    [formatter setTimeZone:inputTimeZone];
    return [formatter dateFromString:dateValue];
}

+ (NSNumber *)getTimestampOfDate:(NSDate *)dateValue{
    return [NSNumber numberWithDouble:([dateValue timeIntervalSinceReferenceDate]*1000)];
}

+ (NSString *)jsonToDictionary:(NSDictionary *)dictionary{
    NSMutableString *returnString;
    NSError *error=nil;
     NSData *jsonData=[NSJSONSerialization dataWithJSONObject:dictionary options:NSJSONWritingPrettyPrinted error:&error];
    
    if (!jsonData) {
        NSLog(@"bv_jsonStringWithPrettyPrint: error: %@", error.localizedDescription);
        returnString=[NSMutableString stringWithString:@"{}"];
        }
    
    else
    {
        returnString =[NSMutableString stringWithString:[[NSString alloc] initWithData:jsonData encoding:NSUTF8StringEncoding]];
    }
    return returnString;
}

+ (NSMutableDictionary *) dictionaryWithPropertiesOfObject:(id)obj
{
    NSMutableDictionary *dict = [NSMutableDictionary dictionary];
    unsigned count;
    //property_getAttributes
    objc_property_t *properties = class_copyPropertyList([obj class], &count);
    
    for (int i = 0; i < count; i++) {
        NSString *key = [NSString stringWithUTF8String:property_getName(properties[i])];
        //NSLog(@"Selvam %s",getPropertyMappedVarName(properties[i]));
        NSString *attributeName=[NSString stringWithFormat:@"%s",getPropertyMappedVarName(properties[i])];
        
        if ([obj valueForKey:key]) {
            [dict setObject:[obj valueForKey:key] forKey:attributeName];
        }
        
        
    }
    free(properties);
    return dict;
}
static const char *getPropertyMappedVarName(objc_property_t property) {
    const char *attributes = property_getAttributes(property);
    char buffer[1 + strlen(attributes)];
    strcpy(buffer, attributes);
    char *state = buffer, *attribute;
    NSString *getterName = nil;
    while ((attribute = strsep(&state, ",")) != NULL) {
        
        if (attribute[0] == 'V' && attribute[1] == '_' && strlen(attribute) > 2) {
            NSString *name = [[NSString alloc] initWithBytes:attribute + 2 length:strlen(attribute) - 2 encoding:NSASCIIStringEncoding];
            return (const char *)[name cStringUsingEncoding:NSASCIIStringEncoding];
        }
        
        else if (attribute[0] == 'V' && strlen(attribute) > 1) {
            NSString *name = [[NSString alloc] initWithBytes:attribute + 1 length:strlen(attribute) - 1 encoding:NSASCIIStringEncoding];
            return (const char *)[name cStringUsingEncoding:NSASCIIStringEncoding];
        }
        
        else if (attribute[0] == 'G' && strlen(attribute) > 1) {
            getterName = [[NSString alloc] initWithBytes:attribute + 1 length:strlen(attribute) - 1 encoding:NSASCIIStringEncoding];
        }
    }
    
    if (getterName) {
        return (const char *)[getterName cStringUsingEncoding:NSASCIIStringEncoding];
    }
    return "";
}

+ (BOOL)validateToday:(NSDate *)dateVale{
    NSDateComponents *otherDay = [[NSCalendar currentCalendar] components:NSCalendarUnitEra|NSCalendarUnitYear|NSCalendarUnitMonth|NSCalendarUnitDay fromDate:dateVale];
    NSDateComponents *today = [[NSCalendar currentCalendar] components:NSCalendarUnitEra|NSCalendarUnitYear|NSCalendarUnitMonth|NSCalendarUnitDay fromDate:[NSDate date]];
    
    if([today day] == [otherDay day] &&
       [today month] == [otherDay month] &&
       [today year] == [otherDay year] &&
       [today era] == [otherDay era]) {
        return TRUE;
    }
    return FALSE;
}

+ (NSDate *)endOfDay:(NSDate *)date
{
    NSCalendar *cal = [NSCalendar currentCalendar];
    NSDateComponents *components = [cal components:(NSCalendarUnitYear | NSCalendarUnitMonth | NSCalendarUnitDay) fromDate:date];
    
    [components setHour:23];
    [components setMinute:59];
    [components setSecond:59];
    
    return [cal dateFromComponents:components];
    
}

+ (NSDate *)beginingOfDay:(NSDate *)date
{
    
    NSCalendar *calendar = [NSCalendar currentCalendar];
    NSDateComponents *components = [calendar components:(NSCalendarUnitYear | NSCalendarUnitMonth | NSCalendarUnitDay) fromDate:date];
    [components setHour:0];
    [components setMinute:0];
    [components setSecond:0];
    return [calendar dateFromComponents:components];
    
}
+ (BOOL)isIOS9{
    
    if (SYSTEM_VERSION_GREATER_THAN_OR_EQUAL_TO(@"9.0")&&SYSTEM_VERSION_LESS_THAN(@"10.0")) {
        return TRUE;
    }
    return FALSE;
}

+(NSArray *)removeNilArrayOfString:(NSArray *)array{
    NSMutableArray *mutableArray=[NSMutableArray arrayWithArray:array];
    [mutableArray removeObjectIdenticalTo:[NSNull null]];
    return mutableArray;
}

+ (void)imageRequestOperation:(NSString *)photoUrl witImagView:(UIImageView *)imageView
{
    // download the photo
    NSURLRequest *request = [NSURLRequest requestWithURL:[NSURL URLWithString:photoUrl]];
    [request setAccessibilityLabel:@"selvam"];
    AFImageRequestOperation *operation = [AFImageRequestOperation imageRequestOperationWithRequest:request imageProcessingBlock:^UIImage *(UIImage *image) {
        return image;
    } success:^(NSURLRequest *request, NSHTTPURLResponse *response, UIImage *image) {
        imageView.image=image;
        
    } failure:^(NSURLRequest *request, NSHTTPURLResponse *response, NSError *error) {
        
    }];
    [operation start];
}
+ (UIColor *)averageColor:(UIImage *)image {
    
    CGColorSpaceRef colorSpace = CGColorSpaceCreateDeviceRGB();
    unsigned char rgba[4];
    CGContextRef context = CGBitmapContextCreate(rgba, 1, 1, 8, 4, colorSpace, kCGImageAlphaPremultipliedLast | kCGBitmapByteOrder32Big);
    
    CGContextDrawImage(context, CGRectMake(0, 0, 1, 1), image.CGImage);
    CGColorSpaceRelease(colorSpace);
    CGContextRelease(context);
    
    if(rgba[3] > 0) {
        CGFloat alpha = ((CGFloat)rgba[3])/255.0;
        CGFloat multiplier = alpha/255.0;
        return [UIColor colorWithRed:((CGFloat)rgba[0])*multiplier
                               green:((CGFloat)rgba[1])*multiplier
                                blue:((CGFloat)rgba[2])*multiplier
                               alpha:alpha];
    }
    
    else {
        return [UIColor colorWithRed:((CGFloat)rgba[0])/255.0
                               green:((CGFloat)rgba[1])/255.0
                                blue:((CGFloat)rgba[2])/255.0
                               alpha:((CGFloat)rgba[3])/255.0];
    }
}

+ (NSDate *)epochToDate:(NSNumber *)stringValue{

    double value=[stringValue doubleValue]/1000;
    return [NSDate dateWithTimeIntervalSince1970:value];
}

+ (NSString *)getLocalCurrencyName{
     AppDelegate *appdelegate=(AppDelegate *)[[UIApplication sharedApplication] delegate];
    NSLocale* localPrice = [[NSLocale alloc] initWithLocaleIdentifier:appdelegate.localLocation];
    NSNumberFormatter *fmtr = [[NSNumberFormatter alloc] init];
    [fmtr setNumberStyle:NSNumberFormatterCurrencyPluralStyle];
    [fmtr setLocale:localPrice];
    NSCharacterSet *numbersSet = [NSCharacterSet characterSetWithCharactersInString:@"0123456789."];
    NSString *trimmedString = [[fmtr stringFromNumber:[NSNumber numberWithInt:100]] stringByTrimmingCharactersInSet:numbersSet];
    trimmedString = [trimmedString stringByTrimmingCharactersInSet:
                     [NSCharacterSet whitespaceAndNewlineCharacterSet]];
    return trimmedString;
    
}

+ (NSString *)getLocalCurrencySymbole{
    AppDelegate *appdelegate=(AppDelegate *)[[UIApplication sharedApplication] delegate];
    NSLocale* localPrice = [[NSLocale alloc] initWithLocaleIdentifier:appdelegate.localLocation];
    NSNumberFormatter *fmtr = [[NSNumberFormatter alloc] init];
    [fmtr setNumberStyle:NSNumberFormatterCurrencyStyle];
    [fmtr setLocale:localPrice];
    NSCharacterSet *numbersSet = [NSCharacterSet characterSetWithCharactersInString:@"0123456789."];
    NSString *trimmedString = [[fmtr stringFromNumber:[NSNumber numberWithInt:100]] stringByTrimmingCharactersInSet:numbersSet];
    trimmedString = [trimmedString stringByTrimmingCharactersInSet:
                     [NSCharacterSet whitespaceAndNewlineCharacterSet]];
    return trimmedString;
}

+ (NSString *)getLocalAddress{
    return @"";
}

+(NSDate *)getLocalTimeValue:(NSDate *)sourceDate{
    
    NSTimeZone* sourceTimeZone = [NSTimeZone timeZoneWithAbbreviation:@"UTC"];
    NSTimeZone* destinationTimeZone = [NSTimeZone systemTimeZone];
    
    NSInteger sourceGMTOffset = [sourceTimeZone secondsFromGMTForDate:sourceDate];
    NSInteger destinationGMTOffset = [destinationTimeZone secondsFromGMTForDate:sourceDate];
    NSTimeInterval interval = destinationGMTOffset - sourceGMTOffset;
    
    NSDate* destinationDate = [[NSDate alloc] initWithTimeInterval:interval sinceDate:sourceDate];
    return destinationDate;
}

+ (NSDate *)getNearestTimeValue{
    NSDate *mydate=[NSDate date];
    NSDateComponents *time = [[NSCalendar currentCalendar]components: NSCalendarUnitHour |NSCalendarUnitMinute fromDate: mydate];
    NSUInteger remainder = ([time minute] % 15);
    mydate = [mydate dateByAddingTimeInterval: 60 * (15 - remainder)];
    return mydate;
}

+ (NSDate *)getNearestTimeValueWithTime:(NSDate *)dateValue{
    NSDate *mydate=dateValue;
    NSDateComponents *time = [[NSCalendar currentCalendar]components: NSCalendarUnitHour |NSCalendarUnitMinute fromDate: mydate];
    NSUInteger remainder = ([time minute] % 15);
    mydate = [mydate dateByAddingTimeInterval: 60 * (15 - remainder)];
    return mydate;
}

+ (NSDate *)mergeDateValue:(NSDate *)dateValue timeValue:(NSDate *)timeValue{
    NSString *dateValueString=[Utility dateToStringFormat:@"MM/dd/yyyy" dateString:dateValue timeZone:UTC];
    NSString *timevalueString=[Utility dateToStringFormat:@"HH:mm:ss" dateString:timeValue timeZone:UTC];
    NSString *mergedTime=[NSString stringWithFormat:@"%@ %@",dateValueString,timevalueString];
    return [Utility stringToDateFormat:@"MM/dd/yyyy HH:mm:ss" dateString:mergedTime  timeZone:UTC];
}

+ (BOOL)validateNilObject:(id)objectValue{
    if ([objectValue isEqual:[NSNull null]]) {
        return TRUE;
    }
    return FALSE;
}
+ (NSDate *)combineDate:(NSDate *)date withTime:(NSDate *)time {
    
    NSCalendar *gregorian = [[NSCalendar alloc] initWithCalendarIdentifier:
                             NSCalendarIdentifierGregorian];
    
    unsigned unitFlagsDate = NSCalendarUnitYear | NSCalendarUnitMonth
    |  NSCalendarUnitDay;
    NSDateComponents *dateComponents = [gregorian components:unitFlagsDate
                                                    fromDate:date];
    unsigned unitFlagsTime = NSCalendarUnitHour | NSCalendarUnitMinute
    |  NSCalendarUnitSecond;
    NSDateComponents *timeComponents = [gregorian components:unitFlagsTime
                                                    fromDate:time];
    
    [dateComponents setSecond:[timeComponents second]];
    [dateComponents setHour:[timeComponents hour]];
    [dateComponents setMinute:[timeComponents minute]];
    
    NSDate *combDate = [gregorian dateFromComponents:dateComponents];   
    
    return combDate;
}

@end
