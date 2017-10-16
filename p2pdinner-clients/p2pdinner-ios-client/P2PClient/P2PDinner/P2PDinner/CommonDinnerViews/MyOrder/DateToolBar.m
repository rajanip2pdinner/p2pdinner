//
//  DateToolBar.m
//  P2PDinner
//
//  Created by Selvam M on 7/26/15.
//  Copyright (c) 2015 P2PDinner. All rights reserved.
//

#import "DateToolBar.h"
#import "Utility.h"
@interface DateToolBar(){
    UIBarButtonItem *lastSelectedBtn;
}
@end
@implementation DateToolBar
@synthesize buttonDelegate;
- (UIImage *)makeImageFromString:(NSString *)ButtonString{
    
    UILabel * addCustomLabel =
    [[UILabel alloc] initWithFrame:CGRectMake(0, 0, 35, 45)];
    addCustomLabel.text=ButtonString;
    addCustomLabel.textColor = [UIColor whiteColor];
    addCustomLabel.font = [UIFont systemFontOfSize:15];
    addCustomLabel.numberOfLines = 2;
    addCustomLabel.backgroundColor = [UIColor clearColor];
    addCustomLabel.textAlignment = NSTextAlignmentCenter;
    
    CGSize size = addCustomLabel.bounds.size;
    
    static CGFloat scale = -1.0;
    
    UIScreen *screen = [UIScreen mainScreen];
    scale = [screen scale];
    
    if(scale > 0.0)
    {
        UIGraphicsBeginImageContextWithOptions(size, NO, scale);
    }
    else
    {
        UIGraphicsBeginImageContext(size);
    }
    
    CGContextRef context = UIGraphicsGetCurrentContext();
    [addCustomLabel.layer renderInContext: context];
    UIImage *img = UIGraphicsGetImageFromCurrentImageContext();
    context=nil;
    
//    CGContextRelease(context);
    
    return img;
}
- (NSString *)getDateString:(int)buttonValue{
    
    return [Utility dateToStringFormat:@"dd   EEE" dateString:[[NSDate date]dateByAddingTimeInterval:(buttonValue*60*60*24)] timeZone:LOCAL];
    
    
}
- (NSDate *)getTodayTwellMorning:(NSDate *)inputDate{
    NSCalendar *calendar = [[NSCalendar alloc] initWithCalendarIdentifier:NSCalendarIdentifierGregorian];
    [calendar setLocale:[NSLocale currentLocale]];
    [calendar setTimeZone:[NSTimeZone localTimeZone]];
    
    NSDateComponents *nowComponents = [calendar components:NSCalendarUnitYear | NSCalendarUnitMonth | NSCalendarUnitDay fromDate:inputDate];
    inputDate = [calendar dateFromComponents:nowComponents];
    return inputDate;
}

- (NSArray *)makeBarButtonArray{
    NSMutableArray *barButtonArray=[[NSMutableArray alloc]init];
    int valueToPass;
    UIImage *setImageValue;
    [barButtonArray addObject:[[UIBarButtonItem alloc] initWithBarButtonSystemItem:UIBarButtonSystemItemFlexibleSpace target:nil action:nil]];
    for (int i=0; i<5; i++) {
        valueToPass=i-(5/2);
        setImageValue=[self makeImageFromString:[self getDateString:valueToPass]];
        UIBarButtonItem *barBtn=[[UIBarButtonItem alloc]initWithImage:setImageValue landscapeImagePhone:setImageValue style:UIBarButtonItemStyleDone target:self action:@selector(barButtonAction:)];
        
       // NSString *timeValue=[NSString stringWithFormat:@"%.0f",([[self getTodayTwellMorning:[[NSDate date]dateByAddingTimeInterval:(valueToPass*60*60*24)]] timeIntervalSince1970]*1000)];
       NSString *timeValue=[Utility dateToStringFormat:@"MM/dd/yyyy HH:mm:ss" dateString:[self getTodayTwellMorning:[[NSDate date]dateByAddingTimeInterval:(valueToPass*60*60*24)]] timeZone:LOCAL];
        
        
        [barBtn setAccessibilityLabel:timeValue];
        
        if (valueToPass==0) {
            [barBtn setTintColor:[UIColor redColor]];
            lastSelectedBtn=barBtn;
        }
        else
            [barBtn setTintColor:[UIColor colorWithRed:237.0/255.0 green:134.0/255.0 blue:0.0/255.0 alpha:1]];
        [barButtonArray addObject:barBtn];
    }
    [barButtonArray addObject:[[UIBarButtonItem alloc] initWithBarButtonSystemItem:UIBarButtonSystemItemFlexibleSpace target:nil action:nil]];
    return barButtonArray;
}
- (void)intDateToolBar{
    self.items=[self makeBarButtonArray];
    
}
- (void)barButtonAction:(UIBarButtonItem *)barButton{
    [barButton setTintColor:[UIColor redColor]];
    [lastSelectedBtn setTintColor:[UIColor colorWithRed:237.0/255.0 green:134.0/255.0 blue:0.0/255.0 alpha:1]];
    lastSelectedBtn=barButton;
    [self.buttonDelegate selectedDateOption:[Utility stringToDateFormat:@"MM/dd/yyyy HH:mm:ss" dateString:barButton.accessibilityLabel timeZone:LOCAL]];
}
@end
