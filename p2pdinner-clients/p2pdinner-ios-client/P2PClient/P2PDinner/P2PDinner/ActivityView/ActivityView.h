//
//  ActivityView.h
//  P2PDinner
//
//  Created by Selvam M on 3/15/15.
//  Copyright (c) 2015 P2PDinner. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface ActivityView : UIView
@property(nonatomic, strong) UILabel *activityLabel;
@property(nonatomic, strong) UIActivityIndicatorView *activityIndicatorView;
@property(assign)            BOOL isAnimating;
@property(nonatomic,strong) UIView *activityViewBorder;

// Call this method to start animation with text
- (void)startAnimating:(NSString *)progressText;
// Call this method to stop animating
- (void)stopAnimating;

@end
