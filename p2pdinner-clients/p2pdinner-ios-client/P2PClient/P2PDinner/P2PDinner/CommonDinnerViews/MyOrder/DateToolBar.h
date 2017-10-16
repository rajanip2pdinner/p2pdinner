//
//  DateToolBar.h
//  P2PDinner
//
//  Created by Selvam M on 7/26/15.
//  Copyright (c) 2015 P2PDinner. All rights reserved.
//

#import <UIKit/UIKit.h>
@protocol BarButtonActionDelegate<NSObject>
-(void)selectedDateOption:(NSDate *)dateValue;
@end
@interface DateToolBar : UIToolbar
@property(nonatomic,retain) id<BarButtonActionDelegate> buttonDelegate;
-(void)intDateToolBar;
@end
