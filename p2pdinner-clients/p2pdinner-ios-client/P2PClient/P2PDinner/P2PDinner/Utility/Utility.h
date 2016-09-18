//
//  Utility.h
//  P2PDinner
//
//  Created by sudheerkumar on 2/6/15.
//  Copyright (c) 2015 P2PDinner. All rights reserved.
//

#import <Foundation/Foundation.h>
#import <CoreGraphics/CoreGraphics.h>
#import <UIKit/UIKit.h>
typedef enum {
    LOCAL,
    UTC
}TimeZone;
@interface Utility : NSObject
+ (NSString *)dateToStringFormat:(NSDate *)dateValue timeZone:(TimeZone)timeZone;
+ (NSString *)jsonStringToURLParameter:(NSString *)jsonString;
+ (NSString *)dictionaryStringToURLParameter:(NSDictionary *)jsonDictionary;
+ (NSDictionary *)jsonString:(NSString *)jsonStr;
+ (CGSize)getSizeOfString:(NSString *)string OfFont:(UIFont *)fontType;
+ (NSDate *)stringToDateFormat:(NSString *)format dateString:(NSString *)dateValue timeZone:(TimeZone)timeZone;
+ (NSString *)dateToStringFormat:(NSString *)format dateString:(NSDate *)dateValue timeZone:(TimeZone)timeZone;
//+ (NSDate *)getLocalToUTC:(NSDate *)local;
//+ (NSDate *)getUTCToLocal:(NSDate *)local;
+ (NSNumber *)getTimestampOfDate:(NSDate *)dateValue;
+ (NSString *)jsonToDictionary:(NSDictionary *)dictionary;
+ (NSMutableDictionary *) dictionaryWithPropertiesOfObject:(id)obj;
+ (BOOL)validateToday:(NSDate *)dateVale;
+ (NSDate *)endOfDay:(NSDate *)date;
+ (BOOL)isIOS9;
+(NSArray *)removeNilArrayOfString:(NSArray *)array;
+ (void)imageRequestOperation:(NSString *)photoUrl witImagView:(UIImageView *)imageView;
+ (UIColor *)averageColor:(UIImage *)image;
+ (NSDate *)epochToDate:(NSNumber *)stringValue;
+ (NSString *)getLocalCurrencyName;
+ (NSString *)getLocalCurrencySymbole;
+ (NSString *)getLocalAddress;
+(NSDate *)getLocalTimeValue:(NSDate *)sourceDate;
+ (NSDate *)beginingOfDay:(NSDate *)date;
@end
