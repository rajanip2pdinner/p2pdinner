//
//  CartRequest.h
//  P2PDinner
//
//  Created by Selvam M on 3/13/16.
//  Copyright © 2016 P2PDinner. All rights reserved.
//

#import <Foundation/Foundation.h>

@interface CartRequest : NSObject
@property(nonatomic,strong)NSString *listing_id;
@property(nonatomic,strong)NSNumber *profile_id;
@property(nonatomic,strong)NSNumber *quantity;
@property(nonatomic,strong)NSString *delivery_type;
@end
