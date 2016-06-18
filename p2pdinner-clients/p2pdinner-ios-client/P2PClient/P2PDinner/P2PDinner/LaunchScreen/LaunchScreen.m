//
//  LaunchScreen.m
//  P2PDinner
//
//  Created by sudheerkumar on 2/25/15.
//  Copyright (c) 2015 P2PDinner. All rights reserved.
//

#import "LaunchScreen.h"

@implementation LaunchScreen

- (void)showLaunchScreen{
    
    UIWindow *window = [[UIApplication sharedApplication].windows   objectAtIndex:0];
    [window addSubview:self.view];
    [self performSelector:@selector(hideModal) withObject:self afterDelay:2];
}

- (void)hideModal
{
    UIView *target = [UIApplication sharedApplication].keyWindow;
    CATransition *animation = [CATransition animation];
    
    [animation setDelegate:self];
    [animation setType:kCATransitionFade];
    [animation setSubtype:kCATransitionFromBottom];
    
    [animation setDuration:0.35];
    [animation setTimingFunction:[CAMediaTimingFunction functionWithName:kCAMediaTimingFunctionEaseInEaseOut]];
    
    [[target layer] addAnimation:animation forKey:kCATransition];
    
    [[[[[[UIApplication sharedApplication] windows] firstObject] subviews] lastObject] removeFromSuperview];
    
    
}

@end
