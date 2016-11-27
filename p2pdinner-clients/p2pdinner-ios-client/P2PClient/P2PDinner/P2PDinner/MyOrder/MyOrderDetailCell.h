//
//  MyOrderDetailCell.h
//  P2PDinner
//
//  Created by Selvam M on 11/24/16.
//  Copyright © 2016 P2PDinner. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "MyOrderItem.h"

@interface MyOrderDetailCell : UITableViewCell
@property(nonatomic,strong)CarRecivedItemDetail *cartDetail;
- (void)setCartDetailValues:(CarRecivedItemDetail *)cartDetail;
- (IBAction)addToCalenderEvent:(id)sender;
@end
