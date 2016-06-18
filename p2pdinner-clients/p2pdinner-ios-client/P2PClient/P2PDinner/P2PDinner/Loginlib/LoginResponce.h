//
//  LoginResponce.h
//  P2PDinner
//
//  Created by Selvam M on 3/6/15.
//  Copyright (c) 2015 P2PDinner. All rights reserved.
//

#import "P2PResponceObject.h"

@interface LoginResponce : P2PResponceObject{
    
}
@property (nonatomic,strong)NSString *message;
@property (nonatomic,strong)NSNumber *status;
@property (nonatomic,strong)NSString *code;
- (LoginResponce *)mapObjectFromResponce:(id)response;
@end
